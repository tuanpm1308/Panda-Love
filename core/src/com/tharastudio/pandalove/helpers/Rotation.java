package com.tharastudio.pandalove.helpers;

public class Rotation {
	private float time;
	private float delay;
	private float stepAngle		= 0;
	private float currentAngle	= 0;

	public Rotation() {

	}

	public Rotation(float stepAngle, float delay) {
		this(0, stepAngle, delay);
	}

	public Rotation(float angle, float stepAngle, float delay) {
		this.currentAngle	= angle;
		this.stepAngle		= stepAngle;
		this.delay			= delay;
	}

	public void update(float dt) {
		if (delay <= 0) {
			return;
		}

		time += dt;

		while (time > delay) {
			step();
		}
	}

	private void step() {
		time			-= delay;
		currentAngle	+= stepAngle;

		if (currentAngle > 360) {
			currentAngle	-= 360;
		}
	}

	public float getStepAngle() {
		return stepAngle;
	}

	public void setStepAngle(float stepAngle) {
		this.stepAngle = stepAngle;
	}

	public float getAngle() {
		return currentAngle;
	}

	public void setAngle(float currentAngle) {
		this.currentAngle = currentAngle;
	}
}
