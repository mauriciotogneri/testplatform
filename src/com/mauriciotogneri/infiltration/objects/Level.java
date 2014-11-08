package com.mauriciotogneri.infiltration.objects;

import com.mauriciotogneri.infiltration.controls.Input;
import com.mauriciotogneri.infiltration.objects.enemies.laser.Laser;
import com.mauriciotogneri.infiltration.utils.Resources;

public class Level
{
	private final Building building;
	private final Protagonist max;
	private final ViewPoint viewPoint;
	private final Laser laser1;
	private final Laser laser2;
	
	public static final int BLOCK_SIZE = 32;
	
	public Level()
	{
		this.building = new Building(Resources.Levels.LEVEL_1, Resources.Images.Levels.LEVEL_1);
		this.building.start();
		
		this.viewPoint = new ViewPoint((this.building.getWidth() + 1) * Level.BLOCK_SIZE);
		this.viewPoint.start();
		
		this.max = new Protagonist(this, this.building, Level.BLOCK_SIZE * 28, Level.BLOCK_SIZE * 15, -1);
		this.max.start();
		
		this.laser1 = new Laser(Level.BLOCK_SIZE * 18, Level.BLOCK_SIZE * 13, 2, 1000, 1000, true);
		this.laser2 = new Laser(Level.BLOCK_SIZE * 20, Level.BLOCK_SIZE * 13, 2, 1000, 1000, true);
		
		reset();
	}
	
	public void update(float delta, Input input)
	{
		this.max.update(delta, input);
		this.viewPoint.update(this.max);
	}
	
	public void reset()
	{
		this.max.reset();
		this.laser1.reset();
		this.laser2.reset();
	}
	
	public void stop()
	{
		this.laser1.stop();
		this.laser2.stop();
	}
}