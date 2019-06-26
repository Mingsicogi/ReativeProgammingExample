import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("deprecation")
public class Main {

    public static void main(String[] args) {
        System.out.println("========= Reactive Programming 01 =========\n");

        System.out.println("========= Iterable =========");
        //List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9,10); // Collection이 Iterable을 상속함. foreach의 구조는 iterable로 구현됨.

        Iterable<Integer> iterator = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        for(Integer num : iterator){
            System.out.println(num);
        }

        System.out.println();
        // 나만의 iterable을 구현해서 foreach사용하기
        Iterable<Integer> customIterable = () ->
            new Iterator<> () {
                int i = 0;
                final static int MAX = 10;

                @Override
                public boolean hasNext() {
                    return i < MAX;
                }

                @Override
                public Integer next() {
                    return ++i;
                }
            };

        for (Integer i : customIterable){ // pull
            System.out.println(i);
        }

        System.out.println("========= Observable =========");

        //Observable : Source -> Event(data) -> Observer
        class IntegerObservable extends Observable implements Runnable{

            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    setChanged(); // Observer에게 noti하기 전에 setChange 호출
                    notifyObservers(i); // push
                }
            }
        }

        Observer observer = (o, arg) -> System.out.println(Thread.currentThread().getName() + " : " + arg); // lambda
//        Observer observer = new Observer() {
//            @Override
//            public void update(Observable o, Object arg) {
//                System.out.println(arg);
//            }
//        };

        IntegerObservable integerObservable = new IntegerObservable();
        integerObservable.addObserver(observer);

//      integerObservable.run();// event action!
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(integerObservable);

        System.out.println(Thread.currentThread().getName() + " : " + "QUIT!!!");
        executorService.shutdown();

        // 기존 Observer pattern의 두가지 문제
        // Complete ?
        // ERROR ?
    }
}
