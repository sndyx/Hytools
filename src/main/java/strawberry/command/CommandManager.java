package strawberry.command;

public class CommandManager {

    private static Command[] commands = new Command[1];
    private static int pointer = 0;

    /**
     * Adds a new command to the CommandManager.
     * 
     * @param literal command literal
     * @return        command
     */
    public static Command command(String literal) {
        Command command = new Command(literal);
        for (int i = 0 ; i < commands.length ; i++) {
            if (commands[i].getLiteral().equals(literal)) {
                command = commands[i];
                break;
            }
        }
        if (pointer == commands.length) {
            increaseBufferSize();
        }
        commands[pointer] = command;
        pointer++;
        return command;
    }

    /**
     * Retuns true if the CommandManager contains
     * the queried command.
     * 
     * @param query command literal
     * @return      does command exist
     */
    public static boolean contains(String query){
        for (Command command : commands) {
            if (command.getLiteral().equals(query)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Executes a given command.
     * 
     * @param literal command literal
     * @param args    command arguments
     */
    public static void execute(String literal, String[] args) {
        for (Command command : commands) {
            if (command.getLiteral().equals(literal)) {
                command.execute(args);
                return;
            }
        }
    }

    public static void reset() {
        commands = new Command[1];
    }

    private static void increaseBufferSize() {
        Command[] increased = new Command[commands.length * 2];
        for (int i = 0 ; i < commands.length ; i++) {
            increased[i] = commands[i];
        }
    }
    
}
