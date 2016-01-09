package odk;

import odk.task.AcceptTask;
import odk.task.PoisonPill;

import static java.util.Arrays.asList;
import static odk.TestRunner.assertTrue;

/**
 * User: operehod
 * Date: 09.01.2016
 * Time: 15:15
 */
public class WorkBoardTest {


    public static void testAcceptTasks() {
        //1. регистрируем 3 accept задачи
        AcceptTask task1 = new AcceptTask(null);
        AcceptTask task2 = new AcceptTask(null);
        AcceptTask task3 = new AcceptTask(null);
        WorkBoard.init(asList(task1, task2, task3));

        //2. одна задача зафейлилась.
        WorkBoard.reportAcceptTaskFail();
        assertTrue(!WorkBoard.tasks().stream().filter(t -> t instanceof PoisonPill).findAny().isPresent());

        //2. две задача зафейлилась.
        WorkBoard.reportAcceptTaskFail();
        assertTrue(!WorkBoard.tasks().stream().filter(t -> t instanceof PoisonPill).findAny().isPresent());

        //4. три задача зафейлилась. В списке задач должна появится спец. задача PoisonPill, которая должна "убить" все потоки
        WorkBoard.reportAcceptTaskFail();
        assertTrue(WorkBoard.tasks().stream().filter(t -> t instanceof PoisonPill).findAny().isPresent());
    }


}
