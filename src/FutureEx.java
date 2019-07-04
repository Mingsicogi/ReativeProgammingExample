import java.util.Objects;
import java.util.concurrent.*;

public class FutureEx {

    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ExceptionCallBack {
        void onError(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String>{

        SuccessCallback sc;
        ExceptionCallBack ec;

        @Override
        protected void done() {
            try {
                sc.onSuccess(get());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, Throwable ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }
    }

	// Future
	// Callback
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ExecutorService es = Executors.newCachedThreadPool();

		CallbackFutureTask f = new CallbackFutureTask(() -> {
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " : " + "Async");

            return "HELLO";

        }, System.out::println, e -> System.out.println("Error : "));

		es.execute(f);
		es.shutdown();

//		Future<String> f = es.submit(() -> {
//
//			Thread.sleep(2000);
//			System.out.println(Thread.currentThread().getName() + " : " + "Async");
//
//			return "HELLO";
//		});

//		System.out.println(f.isDone());
//		Thread.sleep(2100);
//		System.out.println(Thread.currentThread().getName() + " : " + "Exit");
//
//		System.out.println(f.isDone());
//		System.out.println(f.get()); // blocking status...


//        FutureTask<String> f = new FutureTask<>(() -> {
//            Thread.sleep(2000);
//            System.out.println(Thread.currentThread().getName() + " : " + "Async");
//
//            return "HELLO";
//        }) {
//            @Override
//            protected void done() {
//                try{
//                    System.out.println(get());
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//                catch (ExecutionException e){
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        es.execute(f);

	}
}
