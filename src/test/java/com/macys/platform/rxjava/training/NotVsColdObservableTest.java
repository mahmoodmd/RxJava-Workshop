package com.macys.platform.rxjava.training;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.reactivex.Observable;

public class NotVsColdObservableTest {
	
	@Test
	public void testColdObservable() throws Exception {
		Observable<String> observable = Observable.interval(1, TimeUnit.SECONDS).map(x -> "obj-"+x);
	
		observable.subscribe(i -> 
			System.out.println("Subscriber-1 Thread: "+ Thread.currentThread().getName() +", Item : " + i ),
			Throwable::printStackTrace,
			() -> System.out.println("Done")
		);
		
		Thread.sleep(10000);
		
		observable.subscribe(i -> 
			System.out.println("Subscriber-2 Thread: "+ Thread.currentThread().getName() +", Item : " + i ),
			Throwable::printStackTrace,
			() -> System.out.println("Done")
		);
			
		Thread.sleep(10000);
	}

	
	
	@Test
	public void testHotObservable() throws Exception {
		Observable<String> observable = Observable.interval(1, TimeUnit.SECONDS).map(x -> "obj-"+x).publish().autoConnect();
		
		observable.subscribe(i -> 
			System.out.println("Subscriber-1 Thread: "+ Thread.currentThread().getName() +", Item : " + i ),
			Throwable::printStackTrace,
			() -> System.out.println("Done")
		);
		
		Thread.sleep(10000);
		
		observable.subscribe(i -> 
			System.out.println("Subscriber-2 Thread: "+ Thread.currentThread().getName() +", Item : " + i ),
			Throwable::printStackTrace,
			() -> System.out.println("Done")
		);
			
		Thread.sleep(10000);
	}

	
	
	
	
}
