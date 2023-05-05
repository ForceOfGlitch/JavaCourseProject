package ru.croc.project;

import java.util.LinkedList;
import java.util.List;

public class CommandFilter {
    private List<Integer> availableCommands;
    public CommandFilter(boolean isUserPromouted, int publicVariantsCount, int adminVariantsCount){
        if(publicVariantsCount < 1 || adminVariantsCount < 0) throw new IllegalArgumentException("Невозможно создать фильтр с некорректным числом команд");

        availableCommands = new LinkedList<>();

        for (int i = 1; i <= publicVariantsCount; i++){
            availableCommands.add(i);
        }

        if(isUserPromouted) {
            for (int i = publicVariantsCount + 1; i < publicVariantsCount + adminVariantsCount + 1; i++){
                availableCommands.add(i);
            }
        }
    }

    /**
     *
     * @param commandNumber введённый номер команды
     * @return существует ли такая команда в списке доступных для текущего пользователя
     */
    public boolean isCommandExists(int commandNumber){
        return availableCommands.contains(commandNumber);
    }

    /**
     * Проверка на то, что введённая строка является не отрицательным целым числом
     * @param commandLine введённая строка команды
     * @return true, если является и false, если нет
     */
    public boolean isNonNegativeNumber(String commandLine) {
        try{
            int val = Integer.valueOf(commandLine);
            if (val < 0) return false;
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
