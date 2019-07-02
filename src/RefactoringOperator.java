import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reactive Streams - Operators
 * Publisher -> [Data1] -> Operator1 -> [Data2] -> Operator2 -> [Data3] -> Subscriber
 * 1. map (d1 -> f -> d2)
 *
 * @author minssogi
 */

public class RefactoringOperator {

	public static void main(String[] args) {

		Iterable<Integer> iter = Stream.iterate(1, i -> i + 1).limit(10).collect(Collectors.toList()); // 1 ~ MAX until limit value

		Publisher<Integer> mapPub = mapPub(getPublisher(iter), num -> num * 10);
		Publisher<Integer> mapPub2 = mapPub(mapPub, num -> num * 10);

		mapPub2.subscribe(getLogSub());
	}

	private static Publisher<Integer> mapPub(Publisher<Integer> publisher, Function<Integer, Integer> function) {
		return new Publisher<>() {
			@Override
			public void subscribe(Subscriber<? super Integer> subscriber) {
				publisher.subscribe(new DelegateSubscriber(subscriber) {

					@Override
					public void onNext(Integer item) {
						super.onNext(function.apply(item));
					}
				});
			}
		};
	}

	/**
	 * Log Subscriber
	 *
	 * @return
	 */
	private static Subscriber<Integer> getLogSub() {
		return new Subscriber<>() {

			@Override
			public void onSubscribe(Subscription subscription) {
				System.out.println("# onSubscribe");
				subscription.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer item) {
				System.out.println("# onNext : " + item);
			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println("# onError : " + throwable.getMessage());
			}

			@Override
			public void onComplete() {
				System.out.println("# onComplete");
			}
		};
	}

	/**
	 * publisher
	 *
	 * @param iter
	 * @return
	 */
	private static Publisher<Integer> getPublisher(Iterable<Integer> iter) {
		return subscriber -> subscriber.onSubscribe(new Subscription() {
				@Override
				public void cancel() {

				}

				//back pressure
				@Override
				public void request(long n) {

					try {
						iter.forEach(i -> subscriber.onNext(i));
						subscriber.onComplete(); // finish signal
					} catch (Throwable t) {
						subscriber.onError(t);
					}
				}
			});
	}
}
