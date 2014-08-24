package com.mauriciotogneri.wakeupmax.objects;

import com.misty.kernel.Process;

public class WorldBlock extends Process
{
	public WorldBlock(int x, int y, String image)
	{
		super(false, false);
		
		this.x = x;
		this.y = y;
		this.z = 2;
		
		setImage(image);
	}
}