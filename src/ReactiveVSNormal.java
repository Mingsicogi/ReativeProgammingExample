import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReactiveVSNormal {

	public static void main(String[] args) {

		Iterable<Integer> iter = Stream.iterate(1, i -> i + 1).limit(1_000_000).collect(Collectors.toList()); // 1 ~ MAX until limit value

		System.out.println("========= Start ==========");

		ExecutorService es1 = Executors.newSingleThreadExecutor();
		es1.execute(() -> getPub(iter).subscribe(getSub()));

		ExecutorService es2 = Executors.newSingleThreadExecutor();
		es2.execute(() -> normalFunc(iter));
		System.out.println("========= Finish ==========");

		es1.shutdown();
		es2.shutdown();
	}

	/**
	 * 일반 프로그래밍
	 *
	 * @param iterable
	 */
	private static void normalFunc(Iterable<Integer> iterable){
		long startTime = System.currentTimeMillis();


		for (Integer num : iterable){
			long result = num;

			do{
				result--;
			}while (result > 0);
		}

		System.out.println(Thread.currentThread().getName() + " : " + "##### Complete normalFunc ##### ");
		System.out.println(Thread.currentThread().getName() + " : " + "It took " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.");
	}

	/**
	 * 리액티브 프로그래밍
	 *  - Publisher
	 *
	 * @param iterable
	 * @return
	 */
	private static Flow.Publisher getPub(Iterable<Integer> iterable){
		return subscriber -> subscriber.onSubscribe(new Flow.Subscription() {

			@Override
			public void request(long n) {
				iterable.forEach(i -> subscriber.onNext(i));
				subscriber.onComplete();
			}

			@Override
			public void cancel() {

			}
		});
	}

	/**
	 * 리액티브 프로그래밍
	 *  - Subscriber
	 *
	 * @return
	 */
	private static Flow.Subscriber<Integer> getSub(){
		return new Flow.Subscriber<>() {

			long startTime = System.currentTimeMillis();

			@Override
			public void onSubscribe(Flow.Subscription subscription) {
				subscription.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer item) {
				long result = item;

				do{
					result--;
				}while (result > 0);
			}

			@Override
			public void onError(Throwable throwable) {

			}

			@Override
			public void onComplete() {
				System.out.println(Thread.currentThread().getName() + " : " + "##### Complete #####");
				System.out.println(Thread.currentThread().getName() + " : " + "It took " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.");
			}
		};
	}
}
