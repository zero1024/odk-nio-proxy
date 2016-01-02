package odk.task;

import odk.WorkBoard;
import odk.Worker;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 21:55
 * <p>
 * ���� �� ������� ��������� �� ���� AcceptIOTask, �� ������������� ������-������� ���������� �������������.
 * ������� ��������� ������ CloseIOTask ������� ������ ���������� ���� worker-��, ����� ��� ������ ���������� ���� ����������.
 */
public class CloseTask implements Task {
    @Override
    public void register(Worker worker) {
        Thread.currentThread().interrupt();
        WorkBoard.addTask(this);
    }
}
