import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureEx {

	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();

		Thread.sleep(2000);
		System.out.println("Hello");

		System.out.println("Exit");
	}
}
