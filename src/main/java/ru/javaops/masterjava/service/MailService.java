package ru.javaops.masterjava.service;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MailService {

    private static final String OK = "OK";
    private static final String INTERRUPTED_BY_FAULTS_NUMBER = "+++ Interrupted by faults number";
    private static final String INTERRUPTED_BY_TIMEOUT = "+++ Interrupted by timeout";
    private static final String INTERRUPTED_EXCEPTION = "+++ InterruptedException";
    private final ExecutorService mailService = Executors.newFixedThreadPool(8);

    public GroupResult sendToList(final String template, final Set<String> emails) throws Exception {

      List<Future<MailResult>> futures = new ArrayList<Future<MailResult>>();


      //Для такой оптимизации нужен отдельный метод, но тогда он будет вызываться каждый раз тоже, непонятно
        /*
      Callable<MailResult> x = new Callable<MailResult>(){
          //Без лямбды.
          @Override
          public MailResult call() throws Exception {
              return sendToUser(template,email);
          }
      };
      */

      for (String email : emails) {
            //Для получение доп результата для проверки отправки, а не только вызова кол и процесса
            //Введём Future тип переменных, для проверки отправки. Самбит оставляет именно Future<>;

            //Коллабл ананимно реализовываем, вставляю в реализацию реальный процесс.
            Future<MailResult> future = mailService.submit(new Callable<MailResult>() {
                                  //Без лямбды.
                                   @Override
                                   public MailResult call() throws Exception {
                                       return sendToUser(template,email);
                                   }
                               });

            futures.add(future);

            return new Callable<GroupResult>(){
                @Override
                public GroupResult call() throws Exception {
                    return null;
                }
            }.call();


        }


        return new GroupResult(0, Collections.emptyList(), null);
    }


    // dummy realization
    public MailResult sendToUser(String template, String email) throws Exception {
        try {
            Thread.sleep(500);  //delay
        } catch (InterruptedException e) {
            // log cancel;
            return null;
        }
        return Math.random() < 0.7 ? MailResult.ok(email) : MailResult.error(email, "Error");
    }

    public static class MailResult {
        private final String email;
        private final String result;

        private MailResult(String email, String cause) {
            this.email = email;
            this.result = cause;
        }

        private static MailResult ok(String email) {
            return new MailResult(email, OK);
        }

        private static MailResult error(String email, String error) {
            return new MailResult(email, error);
        }

        public boolean isOk() {
            return OK.equals(result);
        }

        @Override
        public String toString() {
            return '(' + email + ',' + result + ')';
        }
    }

    public static class GroupResult {
        private final int success; // number of successfully sent email
        private final List<MailResult> failed; // failed emails with causes
        private final String failedCause;  // global fail cause

        public GroupResult(int success, List<MailResult> failed, String failedCause) {
            this.success = success;
            this.failed = failed;
            this.failedCause = failedCause;
        }

        @Override
        public String toString() {
            return "Success: " + success + '\n' +
                    "Failed: " + failed.toString() + '\n' +
                    (failedCause == null ? "" : "Failed cause" + failedCause);
        }
    }
}