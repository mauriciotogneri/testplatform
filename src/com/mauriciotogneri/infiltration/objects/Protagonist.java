package com.mauriciotogneri.infiltration.objects;

import java.util.List;
import com.mauriciotogneri.infiltration.controls.Input;
import com.mauriciotogneri.infiltration.objects.enemies.laser.LaserBeam;
import com.mauriciotogneri.infiltration.utils.Resources;
import com.misty.graphics.Animation;
import com.misty.kernel.Process;
import com.misty.math.Vector;

public class Protagonist extends Process
{
	private final Level level;
	private final Building building;
	private final Animation animationRunning;
	private final Vector position = new Vector();
	private final Vector acceleration = new Vector();
	private final Vector velocity = new Vector();
	
	private boolean jumping = false;
	private boolean jumpingPressed = false;
	
	private static final float FRICTION = 0.6f;
	private static final int MAX_JUMP_SPEED = Level.BLOCK_SIZE * 17;
	private static final int MAX_FALL_SPEED = -Level.BLOCK_SIZE * 12;
	private static final int GRAVITY = 5 * Protagonist.MAX_FALL_SPEED;
	private static final int MAX_RUNNING_SPEED = Level.BLOCK_SIZE * 6;
	
	public Protagonist(Level level, Building building)
	{
		super(false, true);
		
		this.level = level;
		this.building = building;
		
		this.z = 2;
		
		this.animationRunning = new Animation(0.06f, Resources.Images.Protagonist.CHARACTER_RUNNING_1, Resources.Images.Protagonist.CHARACTER_RUNNING_2, Resources.Images.Protagonist.CHARACTER_RUNNING_3, Resources.Images.Protagonist.CHARACTER_RUNNING_4, Resources.Images.Protagonist.CHARACTER_RUNNING_5);
		
		reset();
	}
	
	public void reset()
	{
		this.x = Level.BLOCK_SIZE * 28;
		this.y = Level.BLOCK_SIZE * 15;
		
		this.orientationHorizontal = -1;
		
		this.position.set(this.x, this.y);
		
		setImage(Resources.Images.Protagonist.CHARACTER_IDLE);
		
		this.animationRunning.reset();
	}
	
	public void update(float delta, Input input)
	{
		processInput(input);
		
		updatePosition(delta);
		
		checkPosition();
		
		checkCollision();
		
		setSprite(delta);
	}
	
	private void checkCollision()
	{
		List<Process> list = getCollisions(LaserBeam.class);
		
		if (!list.isEmpty())
		{
			this.level.reset();
		}
	}
	
	private void setSprite(float delta)
	{
		if (this.velocity.y < 0)
		{
			setImage(Resources.Images.Protagonist.CHARACTER_FALLING);
			this.animationRunning.reset();
		}
		else if ((this.velocity.y > 0) || (this.jumping))
		{
			setImage(Resources.Images.Protagonist.CHARACTER_JUMPING);
			this.animationRunning.reset();
		}
		else if (this.velocity.x != 0)
		{
			setImage(this.animationRunning.getSprite(delta));
		}
		else
		{
			setImage(Resources.Images.Protagonist.CHARACTER_IDLE);
			this.animationRunning.reset();
		}
	}
	
	private void updatePosition(float delta)
	{
		this.acceleration.y = Protagonist.GRAVITY;
		this.acceleration.mul(delta);
		this.velocity.add(this.acceleration);
		
		if (this.acceleration.x > 0)
		{
			this.velocity.x = Protagonist.MAX_RUNNING_SPEED;
		}
		else if (this.acceleration.x < 0)
		{
			this.velocity.x = -Protagonist.MAX_RUNNING_SPEED;
		}
		else if (this.acceleration.x == 0)
		{
			this.velocity.x *= Protagonist.FRICTION;
			
			// TODO: ADJUST LIMIT
			if (Math.abs(this.velocity.x) < 1)
			{
				this.velocity.x = 0;
			}
		}
		
		if (this.velocity.y < Protagonist.MAX_FALL_SPEED)
		{
			this.velocity.y = Protagonist.MAX_FALL_SPEED;
		}
		
		this.position.add(this.velocity.copy().mul(delta));
	}
	
	public void touchGround()
	{
		this.velocity.y = 0;
		this.jumping = false;
	}
	
	public void touchCeiling()
	{
		this.velocity.y = 0;
	}
	
	private void checkPosition()
	{
		float newX = this.position.x;
		float newY = this.position.y;
		
		if (newX != this.x)
		{
			this.x = newX;
			
			if (this.velocity.x < 0)
			{
				this.building.checkLeft(this);
			}
			
			if (this.velocity.x > 0)
			{
				this.building.checkRight(this);
			}
		}
		
		if (newY != this.y)
		{
			this.y = newY;
			
			if (this.velocity.y < 0)
			{
				this.building.checkBottom(this);
			}
			
			if (this.velocity.y > 0)
			{
				this.building.checkTop(this);
			}
		}
		
		this.position.x = this.x;
		this.position.y = this.y;
	}
	
	private void processInput(Input input)
	{
		if (input.up)
		{
			if ((!this.jumpingPressed) && (this.velocity.y == 0))
			{
				playSound(Resources.Audio.Sound.JUMP);
				
				this.jumping = true;
				this.jumpingPressed = true;
				this.velocity.y = Protagonist.MAX_JUMP_SPEED;
			}
		}
		else
		{
			this.jumpingPressed = false;
		}
		
		if (input.left)
		{
			this.orientationHorizontal = -1;
			this.acceleration.x = -1;
		}
		else if (input.right)
		{
			this.orientationHorizontal = 1;
			this.acceleration.x = 1;
		}
		else
		{
			this.acceleration.x = 0;
		}
	}
}