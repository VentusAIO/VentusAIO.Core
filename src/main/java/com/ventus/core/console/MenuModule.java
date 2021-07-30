package com.ventus.core.console;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuModule {
    @Setter
    private static int close_tabs = 0;
    ArrayList<ICommand> commands = new ArrayList<>();
    private String message = "";
    private boolean buttonBack = false;

    public MenuModule() {
    }

    public MenuModule(boolean buttonBack) {
        this.buttonBack = buttonBack;
    }

    public MenuModule(ArrayList<ICommand> commands) {
        this.commands = commands;
    }

    public MenuModule(ICommand main) {
        commands.add(main);
    }

    public MenuModule(String s) {
        this.message = s;
    }

    public MenuModule(String s, boolean b) {
        this.message = s;
        this.buttonBack = b;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static <T> T getObjectFromList(List<T> list, String message) {
        do {
            try {
                if (list.isEmpty()) {
                    throw new ArrayIndexOutOfBoundsException("list empty");
                }
                System.out.println(message);
                int i = 1;
                for (T s : list) {
                    System.out.println((i++) + ". " + s);
                }
                System.out.println(i + ". Назад <--");
                int scan = 0;
                Scanner scanner = new Scanner(System.in);
                try {
                    scan = Integer.parseInt(scanner.nextLine());
                    if (scan == i) return null;
                } catch (Exception ignored) {
                }
                if (scan < 1 || scan > i) throw new Exception("Такого варианта нет");
                return list.get(scan - 1);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } while (true);
    }

    public static <K, V> V getObjectFromMap(Map<K, V> map, String message) {
        do {
            try {
                System.out.println(message);
                int i = 1;
                List<V> list = new ArrayList<>();
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    System.out.println((i++) + ". " + entry.getKey());
                    list.add(entry.getValue());
                }
                System.out.println(i + ". Назад <--");
                Scanner scanner = new Scanner(System.in);
                int scan = Integer.parseInt(scanner.nextLine());
                if (scan == i) return null;
                if (scan < 1 || scan > i) throw new Exception("Такого варианта нет");
                return list.get(scan - 1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    public static boolean YesOrNO(String message) {
        do {
            try {
                System.out.println(message + "(Yes/No)");
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("да") || s.equalsIgnoreCase("y") || s.equalsIgnoreCase("д")) {
                    return true;
                } else if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("нет") || s.equalsIgnoreCase("n") || s.equalsIgnoreCase("н")) {
                    return false;
                }
            } catch (Exception ignored) {
            }
        } while (true);
    }

    public void setCommands(ArrayList<ICommand> commands) {
        this.commands = commands;
    }

    public void addCommand(ICommand command) {
        this.commands.add(command);
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                if (close_tabs != 0) {
                    close_tabs--;
                    return;
                }
                if (!message.equals("")) System.out.println(message);
                else System.out.println("Меню:");
                int i = 1;
                for (ICommand command : commands) {
                    System.out.println(i++ + ". " + command.getMessage());
                }
                if (buttonBack) {
                    System.out.println((commands.size() + 1) + ". Назад <--");
                }
                if (scanner.hasNext()) {
                    String result = scanner.nextLine();

                    if (isNumeric(result) && Integer.parseInt(result) > 0 && Integer.parseInt(result) <= commands.size() + 1) {
                        if (Integer.parseInt(result) != commands.size() + 1) {
                            commands.get(Integer.parseInt(result) - 1).execute();
                        } else if (buttonBack && Integer.parseInt(result) == commands.size() + 1) {
                            return;
                        } else {
                            System.out.println("Такого варианта нет. Попробуйте ещё раз");
                        }
                    } else {
                        boolean isBreak = false;
                        for (ICommand command : commands) {
                            if (command.getMessage().equals(result)) {
                                command.execute();
                                isBreak = true;
                                break;
                            }
                        }
                        if (!isBreak) {
                            System.out.println("Такого варианта нет. Попробуйте ещё раз");
                        }
                    }
                } else {
                    System.out.println("Завершение работы");
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
