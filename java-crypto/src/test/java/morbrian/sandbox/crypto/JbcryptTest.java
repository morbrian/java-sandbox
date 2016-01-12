package morbrian.sandbox.crypto;

import org.mindrot.jbcrypt.BCrypt;

public class JbcryptTest {

  public static void test() {
    String sys = "system";
    String sysHash = BCrypt.hashpw(sys, BCrypt.gensalt());
    System.out.println("(" + sys + ") = (" + sysHash + ")");

    String assumedSysHash = "$2a$10$jWw.zFmjUYsH97LMD0up.uX56KzaYTYiolkE9JEEy5osPLrjNL2eq";
    boolean sysOk = BCrypt.checkpw(sys, assumedSysHash);
    System.out.println("isok = " + sysOk);


    String blank = "";
    String blankHash = BCrypt.hashpw(blank, BCrypt.gensalt());
    System.out.println("(" + blank + ") = (" + blankHash + ")");

    String assumedBlankHash = "$2a$10$8qerU5aQnLSw2MDCm61GyO6cIILB//c/8ra2lMtbmnXz5TbRxuQx6";

    boolean blankOk = BCrypt.checkpw(blank, assumedBlankHash);
    System.out.println("isok = " + blankOk);

  }

  public static void main(String[] args) {
    test();
  }

}
