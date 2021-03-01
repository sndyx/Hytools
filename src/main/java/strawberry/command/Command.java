package strawberry.command;

import strawberry.util.Executable;

public class Command {

    private final String literal;
    private Executable[] executables;
    private int pointer = 0;

    Command(String literal) {
        this.literal = literal;
        executables = new Executable[1];
    }

    /**
     * Adds an executable to the command.
     * 
     * @param executable executable
     * @return           instance
     */
    public Command executes(Executable executable) {
        if (pointer == executables.length) {
            increaseBufferSize();
        }
        executables[pointer] = executable;
        pointer++;
        return this;
    }

    public String getLiteral() {
        return literal;
    }

    public Executable[] getExecutables() {
        return executables;
    }

    public void execute(String[] args) {
        for (Executable executable : executables) {
            executable.execute(args);
        }
    }

    private void increaseBufferSize() {
        Executable[] increased = new Executable[executables.length * 2];
        for (int i = 0 ; i < executables.length ; i++) {
            increased[i] = executables[i];
        }
    }
    
}