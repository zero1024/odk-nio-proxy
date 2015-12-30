package odk;

import odk.api.IOTask;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 21:55
 * <p>
 * ���� �� ������� ��������� �� ���� AcceptIOTask, �� ������������� ������-������� ���������� �������������.
 * ������� ��������� ������ CloseIOTask ������� ������ ���������� ���� worker-��, ����� ��� ������ ���������� ���� ����������.
 */
public class CloseIOTask implements IOTask {
    @Override
    public void register(Worker worker) {
        Thread.currentThread().interrupt();
        Board.addTask(this);
    }
}
