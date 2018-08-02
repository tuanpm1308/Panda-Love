package com.tharastudio.pandalove.worlds;

import static com.tharastudio.pandalove.helpers.B2DVars.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.tharastudio.pandalove.Game;
import com.tharastudio.pandalove.entities.*;
import com.tharastudio.pandalove.helpers.B2DVars;
import com.tharastudio.pandalove.helpers.ContactListener;

public class GameWorld {
	private World world;
	private ContactListener contactListener;

	private float screenWidth, screenHeight;

	private TiledMap tiledMap;

	private Player player;
	private Array<Key> keys;
	private Array<Trap> traps;
	private Array<Plat> plats;
	private Array<Bar> bars;
	private Hole hole;

	private int currentLevel;

	public GameWorld() {
		// Play sound
		Music music	= Game.assets.getMusic("song");

		music.setLooping(true);
		music.play();

		startLevel(1);
	}

	public void startLevel(int level) {

		if (!Gdx.files.internal("maps/level" + level + ".tmx").exists()) {
			level	= 1;
		}

		currentLevel	= level;

		// Create box2d world
		world			= new World(new Vector2(0, -9.81f), true);
		contactListener	= new ContactListener();
		world.setContactListener(contactListener);

		hole		= null;

		// Load tiled map
		tiledMap	= new TmxMapLoader().load(Gdx.files.internal("maps/level" + level + ".tmx").toString());

		// create walls
		createWalls();

		// create player
		createPlayer();

		// create keys
		createKeys();

		// create traps
		createTraps();

		// create plats
		createPlats();

		// create bars
		createBars();
	}

	public void update(float delta) {
		world.step(Game.STEP, 1, 1);

		if (contactListener.isDeath()) {
			// Play sound
			Game.assets.getSound("wrong").play();

			// Add effect

			startLevel(currentLevel);
		}

		if (contactListener.isPassed()) {
			// Play sound
			Game.assets.getSound("start").play();

			// Add effect

			startLevel(++currentLevel);
		}

		// check for collected keys
		Array<Body> bodies = contactListener.getBodiesToRemove();
		for(int i = 0; i < bodies.size; i++) {
			Body body = bodies.get(i);
			keys.removeValue((Key) body.getUserData(), true);
			world.destroyBody(bodies.get(i));

			Game.assets.getSound("coin").play();
		}
		bodies.clear();

		// if user collect all the keys, the holes will appear
		if (keys.size == 0 && hole == null) {
			createHole();
		}

		// update player
		player.update(delta);

		// update key
		for (int i = 0; i < keys.size; i++) {
			keys.get(i).update(delta);
		}

		// update trap
		for (int i = 0; i < traps.size; i++) {
			traps.get(i).update(delta);
		}

		// update plats
		for (int i = 0; i < plats.size; i++) {
			plats.get(i).update(delta);
		}

		// update bars
		for (int i = 0; i < bars.size; i++) {
			bars.get(i).update(delta);
		}

		// update hole
		if (hole != null) {
			hole.update(delta);
		}
	}

	// Create bars
	private void createBars() {
		// Create list of bars
		bars	= new Array<Bar>();

		// Get all keys in "keys" layer
		MapLayer mapLayer	= tiledMap.getLayers().get("bars");

		if (mapLayer == null) {
			return;
		}

		for (MapObject mo : mapLayer.getObjects()) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			Rectangle rect = ((RectangleMapObject) mo).getRectangle();
			boolean type = rect.y > 20;

			float x = rect.x / PPM;
			float y = rect.y / PPM;
			float w = rect.width / PPM;
			float h = rect.height / PPM;

			bodyDef.position.set(x + w / 2, y + h / 2);
			Body body = world.createBody(bodyDef);
			FixtureDef fixtureDef = new FixtureDef();

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(w / 2, h / 2);

			fixtureDef.shape = shape;

			body.createFixture(fixtureDef).setUserData("bar");

			// create sensor fixtures
			ChainShape chainShape;
			Vector2[] v	= new Vector2[2];
			float y1, y2;

			if (type == Bar.TYPE_TOP) {
				y1	= - h / 2 + 3 / PPM;
				y2	= y1 + h - 3 / PPM;
			} else {
				y1	= - h / 2;
				y2	= y1 - 3 / PPM + h;
			}

			v[0]	= new Vector2(- w / 2, y1);
			v[1]	= new Vector2(- w / 2, y2);

			chainShape		= new ChainShape();
			chainShape.createChain(v);

			fixtureDef.friction		= 0;
			fixtureDef.shape		= chainShape;

			body.createFixture(fixtureDef).setUserData("wall.right");
			chainShape.dispose();

			v[0]	= new Vector2(w / 2, y1);
			v[1]	= new Vector2(w / 2, y2);

			chainShape		= new ChainShape();
			chainShape.createChain(v);

			fixtureDef.friction		= 0;
			fixtureDef.shape		= chainShape;

			body.createFixture(fixtureDef).setUserData("wall.left");
			chainShape.dispose();

			Bar bar = new Bar(body, type, w, h);
			body.setUserData(bar);

			bars.add(bar);
			shape.dispose();
		}
	}

	// Create plats
	private void createPlats() {
		// Create list of plats
		plats	= new Array<Plat>();

		// Get all keys in "keys" layer
		MapLayer mapLayer	= tiledMap.getLayers().get("plats");

		if (mapLayer == null) {
			return;
		}

		for (MapObject mo : mapLayer.getObjects()) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			Rectangle rect = ((RectangleMapObject) mo).getRectangle();

			float x = rect.x / PPM;
			float y = rect.y / PPM;
			float w = rect.width / PPM;
			float h = rect.height / PPM;

			bodyDef.position.set(x + w / 2, y + h / 2);
			Body body = world.createBody(bodyDef);
			FixtureDef fixtureDef = new FixtureDef();
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(w / 2, h / 2);

			fixtureDef.shape = shape;

			body.createFixture(fixtureDef).setUserData("plat");

			Plat plat = new Plat(body);
			body.setUserData(plat);

			plats.add(plat);
			shape.dispose();
		}
	}

	// Create hole
	private void createHole() {
		// Get all holes in "hole" layer
		MapLayer mapLayer	= tiledMap.getLayers().get("hole");

		if (mapLayer == null) {
			return;
		}

		for (MapObject mo : mapLayer.getObjects()) {
			BodyDef bodyDef	= new BodyDef();
			bodyDef.type	= BodyType.StaticBody;
			Ellipse ellipse	= ((EllipseMapObject) mo).getEllipse();

			float x		= ellipse.x / PPM;
			float y 	= ellipse.y / PPM;
			float w		= ellipse.width / PPM;
			float h		= ellipse.height / PPM;

			bodyDef.position.set(x + w / 2, y + h / 2);
			Body body	= world.createBody(bodyDef);
			FixtureDef fixtureDef	= new FixtureDef();
			CircleShape shape		= new CircleShape();
			shape.setRadius(w / 4);

			fixtureDef.shape	= shape;
			//fixtureDef.isSensor	= true;

			body.createFixture(fixtureDef).setUserData("hole");

			hole	= new Hole(body);
			body.setUserData(hole);

			shape.dispose();

			break;
		}
	}

	// Create traps
	private void createTraps() {
		// Create list of traps
		traps	= new Array<Trap>();

		// Get all traps in "traps" layer
		MapLayer mapLayer	= tiledMap.getLayers().get("traps");

		if (mapLayer == null) {
			return;
		}

		for (MapObject mo : mapLayer.getObjects()) {
			BodyDef bodyDef	= new BodyDef();
			bodyDef.type	= BodyType.StaticBody;
			Rectangle rect	= ((RectangleMapObject) mo).getRectangle();

			float x		= rect.x / PPM;
			float y 	= rect.y / PPM;
			float w		= rect.width / PPM;
			float h		= rect.height / PPM;

			bodyDef.position.set(x + w / 2, y + h / 2);
			Body body	= world.createBody(bodyDef);
			FixtureDef fixtureDef	= new FixtureDef();
			PolygonShape shape		= new PolygonShape();
			shape.setAsBox(w / 2, h / 2);

			fixtureDef.shape	= shape;
			//fixtureDef.isSensor	= true;

			body.createFixture(fixtureDef).setUserData("trap");

			Trap trap	= new Trap(body, w, h);
			body.setUserData(trap);

			traps.add(trap);
			shape.dispose();
		}
	}

	// Create keys
	private void createKeys() {
		// Create list of keys
		keys	= new Array<Key>();

		// Get all keys in "keys" layer
		MapLayer mapLayer	= tiledMap.getLayers().get("keys");

		if (mapLayer == null) {
			return;
		}

		for (MapObject mo : mapLayer.getObjects()) {
			BodyDef bodyDef	= new BodyDef();
			bodyDef.type	= BodyType.StaticBody;
			Rectangle rect	= ((RectangleMapObject) mo).getRectangle();

			float x		= rect.x / PPM;
			float y 	= rect.y / PPM;
			float w		= rect.width / PPM;
			float h		= rect.height / PPM;

			bodyDef.position.set(x + w / 2, y + h / 2);
			Body body	= world.createBody(bodyDef);
			FixtureDef fixtureDef	= new FixtureDef();
			PolygonShape shape		= new PolygonShape();
			shape.setAsBox(w / 2, h / 2);

			fixtureDef.shape	= shape;
			fixtureDef.isSensor	= true;

			body.createFixture(fixtureDef).setUserData("key");

			Key key	= new Key(body);
			body.setUserData(key);

			keys.add(key);
			shape.dispose();
		}
	}

	// Create player
	private void createPlayer() {
		BodyDef bodyDef	= new BodyDef();
		bodyDef.type	= BodyType.DynamicBody;

		bodyDef.position.set(25 / PPM, 25 / PPM);
		bodyDef.fixedRotation	= true;

		Body body	= world.createBody(bodyDef);

		PolygonShape shape	= new PolygonShape();
		shape.setAsBox(6.5f / PPM, 8 / PPM);

		FixtureDef fixtureDef	= new FixtureDef();
		fixtureDef.shape		= shape;
		fixtureDef.density		= 1;
		fixtureDef.friction		= 0;
		fixtureDef.filter.categoryBits	= B2DVars.BIT_PLAYER;

		body.createFixture(fixtureDef).setUserData("player");

		// add foot sensor fixture
		shape.setAsBox(1 / PPM, 1 / PPM, new Vector2(0, -8 / PPM), 0);
		fixtureDef.isSensor		= true;

		body.createFixture(fixtureDef).setUserData("player.foot");

		shape.dispose();

		// create new player
		player	= new Player(body, this);
		body.setUserData(player);
	}

	// Create walls
	private void createWalls() {
		Body body;
		BodyDef bodyDef	= new BodyDef();
		bodyDef.type	= BodyType.StaticBody;
		Vector2[] v		= new Vector2[2];
		ChainShape chainShape;
		FixtureDef fixtureDef	= new FixtureDef();

		bodyDef.position.set(0, 0);
		body					= world.createBody(bodyDef);

		// Create top wall
		v[0]			= new Vector2(0, Game.V_HEIGHT / PPM - 16 / PPM);
		v[1]			= new Vector2(Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM - 16 / PPM);

		chainShape		= new ChainShape();
		chainShape.createChain(v);

		fixtureDef.friction		= 0;
		fixtureDef.shape		= chainShape;

		body.createFixture(fixtureDef).setUserData("wall.top");
		chainShape.dispose();

		// Create bottom wall
		v[0]			= new Vector2(0, 14 / PPM);
		v[1]			= new Vector2(Game.V_WIDTH / PPM, 14 / PPM);

		chainShape		= new ChainShape();
		chainShape.createChain(v);
		fixtureDef.shape		= chainShape;

		body.createFixture(fixtureDef).setUserData("wall.bottom");
		chainShape.dispose();

		// Create left wall
		v[0]			= new Vector2(16 / PPM, 0);
		v[1]			= new Vector2(16 / PPM, Game.V_HEIGHT / PPM);

		chainShape		= new ChainShape();
		chainShape.createChain(v);
		fixtureDef.shape		= chainShape;

		body.createFixture(fixtureDef).setUserData("wall.left");
		chainShape.dispose();

		// Create right wall
		v[0]			= new Vector2(Game.V_WIDTH / PPM - 16 / PPM, 0);
		v[1]			= new Vector2(Game.V_WIDTH / PPM - 16 / PPM, Game.V_HEIGHT / PPM);

		chainShape		= new ChainShape();
		chainShape.createChain(v);
		fixtureDef.shape		= chainShape;

		body.createFixture(fixtureDef).setUserData("wall.right");
		chainShape.dispose();
	}

	public World getWorld() {
		return world;
	}

	public Player getPlayer() {
		return player;
	}

	public Array<Key> getKeys() {
		return keys;
	}

	public Array<Trap> getTraps() {
		return traps;
	}

	public Array<Plat> getPlats() {
		return plats;
	}

	public Array<Bar> getBars() {
		return bars;
	}

	public Hole getHole() {
		return hole;
	}

	public boolean playerCanJump() {
		return contactListener.playerCanJump();
	}
}
