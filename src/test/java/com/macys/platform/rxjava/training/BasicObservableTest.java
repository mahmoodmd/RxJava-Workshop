package com.macys.platform.rxjava.training;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class BasicObservableTest {

    @Test
    public void testBasicObservable() throws Exception {
    	System.out.println("========= testBasicObservable ===========");
        Observable<Integer> observable = Observable.create(observableEmitter -> {
            observableEmitter.onNext(50);
            observableEmitter.onNext(100);
            observableEmitter.onComplete();
        });

        Observer<Integer> observer = new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        };

        observable.subscribe(observer);

       /* Observable.<Integer>create(observableEmitter -> {
            observableEmitter.onNext(50);
            observableEmitter.onNext(100);
            observableEmitter.onComplete();
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }
            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
            @Override
            public void onComplete() {
                System.out.println("Done.");
            }
        });


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(50);
                observableEmitter.onNext(100);
                observableEmitter.onComplete();
            }
        });*/
    }

    @Test
    public void testBasicObservableWithActions() throws Exception {
    	System.out.println("========= testBasicObservableWithActions ===========");
        Observable<Integer> observable = Observable.<Integer>create(observableEmitter -> {
            System.out.println("In observable "+Thread.currentThread().getName());
            observableEmitter.onNext(50);
            observableEmitter.onNext(100);
            //observableEmitter.onError(new RuntimeException("I am Error")); //throw new RuntimeException("I am Error");
            observableEmitter.onNext(150);
            observableEmitter.onComplete();
        });

        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("In onNext 1:"+Thread.currentThread().getName());
                System.out.println(integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.getMessage();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("Done");
            }
        });

        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("In onNext 2 :"+Thread.currentThread().getName());
                System.out.println(integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.getMessage();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("Done");
            }
        });


        /*Observable.<Integer>create(observableEmitter -> {
            observableEmitter.onNext(50);
            observableEmitter.onNext(100);
            observableEmitter.onComplete();
        }).subscribe(x -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(x);},
                Throwable::printStackTrace,
                () -> System.out.println("Done!"));*/

    }
    
    @Test
    public void testBasicObservableWithActionsAndMapAndRepeat() throws Exception {
    	System.out.println("========= testBasicObservableWithActionsAndMapAndRepeat ===========");
        Observable<Integer> observable = Observable.<Integer>create(observableEmitter -> {
            System.out.println("In observable"+Thread.currentThread().getName());
            observableEmitter.onNext(50);
            observableEmitter.onNext(100);
            //observableEmitter.onError(new RuntimeException("I am Error")); //throw new RuntimeException("I am Error");
            observableEmitter.onNext(150);
            observableEmitter.onComplete();
        });


        observable.map(x-> x+10).subscribe( x -> {
            System.out.println("Inside onNext #1:"+Thread.currentThread().getName());
            System.out.println(x);
            }
        	, t-> System.out.format("Got an error with message#1 : %s", t.getMessage())
        	, () -> System.out.println("Done!")
        );


		observable.subscribe(x -> {
			System.out.println("Inside OnNext #2:" + Thread.currentThread().getName());
			System.out.println(x);
		}
		, t -> System.out.format("Got an error with message #2: %s", t.getMessage())
		, () -> System.out.println("Done!")
		);
		   
    }
    
    
    @Test
    public void testVerySimplified(){
    	System.out.println("========= testVerySimplified ===========");
    	Observable.just(50, 100, 150).subscribe(System.out::println);
    }
    
    @Test
    public void testWithInterval() throws InterruptedException{
    	System.out.println("========= testWithInterval ===========");
    	Observable.interval(1, TimeUnit.SECONDS).subscribe(System.out::println);
    	//Observable.interval(2, TimeUnit.SECONDS).subscribe(System.out::println);
    	//Observable.interval(5, TimeUnit.SECONDS).subscribe(System.out::println);
   
    	Observable
    	.interval(1, TimeUnit.SECONDS)
    	.doOnNext(i ->  System.out.println(Thread.currentThread().getName()))
    	.subscribe(System.out::println);
    
    	Observable
    	.interval(1, TimeUnit.SECONDS, Schedulers.newThread())
    	.doOnNext(i ->  System.out.println(Thread.currentThread().getName()))
    	.subscribe(System.out::println);
    	
    	Thread.sleep(20000);
    }
    
    @Test
    public void testRange() throws InterruptedException{
    	System.out.println("========= testRange 1 ===========");
    	Observable.range(1, 100).subscribe(System.out::println);
    	
    	System.out.println("========= testRange 2 ===========");
    	Observable.range(10, 10).subscribe(System.out::println);
    	
    	System.out.println("========= testRange 3 ===========");
    	Observable
    		.range(0, 100)
    		.filter(i -> i % 2 == 0)
    		.subscribe(System.out::println);
    }
    
    @Test
    public void testFutures() throws InterruptedException{
    	ExecutorService executorService = Executors.newFixedThreadPool(5);
    	Future<Integer> future = executorService.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				System.out.println(Thread.currentThread().getName());
				Thread.currentThread().sleep(5000);
				return 50 + 900;
			}
    		
    	});
    	
    	
    	/*Observable
    		.fromFuture(future)
    		.map(x -> "yay! "+ x)
    		.subscribe(System.out::println);
    	//Thread.sleep(50000);
    	*/
    	
    	Observable
		.fromFuture(future)
		.doOnNext(x -> System.out.println(Thread.currentThread().getName()))
		.map(x -> "yay! "+ x)
		.subscribe(System.out::println);
	
    	
    	Observable
		.fromFuture(future)
		.subscribeOn(Schedulers.newThread())
		.doOnNext(x -> System.out.println(Thread.currentThread().getName()))
		.map(x -> "yay! "+ x)
		.subscribe(System.out::println);
    	
    	
    	System.out.println("Continuing on");
    	Thread.sleep(50000);
    	
    }
    
    public Observable<String> getDataFor(String stockName) throws IOException {
    		URL url = new URL("https://www.google.com/finance/historical?q="+stockName+"&output=CSV");
    		InputStream inputStream = url.openConnection().getInputStream();
    		InputStreamReader reader = new InputStreamReader(inputStream);
    		BufferedReader bufferedReader = new BufferedReader(reader);
    		return Observable.create(e -> {
				bufferedReader.lines().forEach(s -> e.onNext(s));
				e.onComplete();
			});

    }
    
    public void log(String str) {
    		System.out.println(str);
    }
    
    @Test
    public void testRealLifeFlatMap() throws Exception {
    		
    		//Observable<String> m = getDataFor("M");
		//m.subscribe(System.out::println);
    	
    		System.out.println("rawData");
    		Observable<String> stockSymbols = Observable.just("GOOG", "M", "AAPL", "MAC", "ORCL");
    		Observable<String>  rawData = stockSymbols
    				.flatMap(ss -> getDataFor(ss).skip(1).take(1)).cache();
    		//cache response
    		
    		System.out.println("zip");
    		Observable<Tuple<String,String>> tupleObservable = 
    				stockSymbols.zipWith(rawData, (s, s2) -> new Tuple<>(s, s2));
    				//.map(t -> new Tuple(t.getU(), Arrays.asList(new StringTokenizer(t.getV()))))
    		
    		System.out.println("print -1 ");
    		tupleObservable.doOnNext( t -> System.out.format("We are processing tuple %s", t))
    			.subscribe(System.out::println,
    				Throwable::printStackTrace,
    				()-> System.out.println("Done"));

    		System.out.println("print -2 ");
    		tupleObservable.doOnNext( t -> System.out.format("We are processing tuple %s", t))
    			.subscribe(System.out::println,
    				Throwable::printStackTrace,
    				()-> System.out.println("Done"));
    		
    		System.out.println("print -3 ");
    		tupleObservable.doOnNext( t -> System.out.format("We are processing tuple %s", t))
    			.subscribe(System.out::println,
    				Throwable::printStackTrace,
    				()-> System.out.println("Done"));
    		
    		
    		Thread.sleep(50000);
    }
    
    
    @Test
    public void testMaterialize() {
    	 Observable<Integer> observable = Observable.<Integer>create(observableEmitter -> {
             System.out.println("In observable "+Thread.currentThread().getName());
             observableEmitter.onNext(50);
             observableEmitter.onNext(100);
             //observableEmitter.onError(new RuntimeException("I am Error")); //throw new RuntimeException("I am Error");
             observableEmitter.onNext(150);
             observableEmitter.onComplete();
         });
    }
}
