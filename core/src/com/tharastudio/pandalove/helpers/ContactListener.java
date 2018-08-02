package com.tharastudio.pandalove.helpers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.tharastudio.pandalove.entities.Player;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private int numFootContacts	= 0;
	private Array<Body> bodiesToRemove;
	public boolean death	= false;
	public boolean passed = false;

	public ContactListener() {
		super();

		bodiesToRemove	= new Array<Body>();
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		if (fixtureA == null || fixtureB == null) {
			return;
		}

		if ((fixtureA.getUserData().equals("wall.right") || fixtureA.getUserData().equals("wall.left")) || fixtureB.getUserData().equals("wall.right") || fixtureB.getUserData().equals("wall.left")) {
			Player player	= (Player) fixtureB.getBody().getUserData();
			player.changeDirection();
		}

		if (fixtureA.getUserData().equals("wall.bottom") && fixtureB.getUserData().equals("player.foot")) {
			numFootContacts++;
		}

		if (fixtureA.getUserData().equals("player.foot") && fixtureB.getUserData().equals("plat")) {
			numFootContacts++;
		}

		if (fixtureB.getUserData().equals("key")) {
			bodiesToRemove.add(fixtureB.getBody());
		}

		if (fixtureB.getUserData().equals("trap")) {
			death = true;
		}

		if (fixtureB.getUserData().equals("hole")) {
			passed = true;
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA	= contact.getFixtureA();
		Fixture fixtureB	= contact.getFixtureB();

		if (fixtureA == null || fixtureB == null || fixtureA.getUserData() == null || fixtureB.getUserData() == null) {
			return;
		}

		if (fixtureA.getUserData().equals("wall.bottom") && fixtureB.getUserData().equals("player.foot")) {
			numFootContacts--;
		}

		if (fixtureA.getUserData().equals("player.foot") && fixtureB.getUserData().equals("plat")) {
			numFootContacts--;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	public Array<Body> getBodiesToRemove() {
		return bodiesToRemove;
	}

	public boolean isDeath() {
		return death;
	}

	public boolean isPassed() {
		return passed;
	}

	public boolean playerCanJump() {
		return numFootContacts > 0;
	}
}
