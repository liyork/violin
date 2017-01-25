package com.wolf.test.thread.productandconsumer.onetoone.usesynchronized;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/12
 * Time: 14:33
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Producer implements Runnable
{
	private Clerk clerk ;

	public Producer(Clerk clerk)
	{
		this.clerk = clerk;
	}

	public void run()
	{
		System.out.println("生产者开始生产产品.");
		while(true)
		{
			try
			{
				Thread.sleep((int)(Math.random() * 10) * 100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			clerk.addProduect(); //生产产品
		}
	}
}