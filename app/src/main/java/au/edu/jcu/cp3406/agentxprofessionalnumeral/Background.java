package au.edu.jcu.cp3406.agentxprofessionalnumeral;

/**
* Used to execute code on background threads
*/
class Background {
    static void run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
