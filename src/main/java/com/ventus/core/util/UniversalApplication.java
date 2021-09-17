package com.ventus.core.util;

import com.ventus.core.interfaces.IProfile;
import com.ventus.core.interfaces.IProxy;
import com.ventus.core.interfaces.ITask;
import com.ventus.core.models.Profile;
import com.ventus.core.models.Proxy;
import com.ventus.core.models.TaskGroup;
import com.ventus.core.models.TaskStatus;
import com.ventus.core.network.AvailabilityFilters;
import com.ventus.core.proxy.ProxyStatus;
import com.ventus.core.task.RequestModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.ventus.core.util.Random.*;

@Slf4j
public class UniversalApplication implements Runnable {
    private String item_id;
    private Class<? extends RequestModule> module;
    private String card_number;
    private String mm;
    private String year;
    private String cvc;
    private final List<IProxy> proxies = new LinkedList<>();
    private final List<String> emails = new ArrayList<>();
    private final List<String> first_names = new ArrayList<>();
    private final List<String> last_names = new ArrayList<>();
    private final List<String> address = new ArrayList<>();
    {
        for(int i = 0; i < 100; i++){
            emails.add(generateRandomWords(15)+getRandom(2, 999)+"@gmail.com");
        }
        first_names.add("Сова");
        first_names.add("Соха");
        first_names.add("Савва");
        first_names.add("Алексей");
        first_names.add("Рома");
        first_names.add("Данил");

        last_names.add("Козлов");
        last_names.add("Петухов");
        last_names.add("Синицын");
        last_names.add("Сидоров");
        last_names.add("Волков");
        last_names.add("Синий");
        last_names.add("Большой");

        address.add("Тверская");
        address.add("Победы");
        address.add("Ленина");
        address.add("Тверская");
        address.add("Тверская");
    }
    public UniversalApplication(String item_id, Class<? extends RequestModule> module) {
        this.item_id = item_id;
        this.module = module;
    }

    public void setCard(String card_number, String mm, String year, String cvc){
        this.card_number = card_number;
        this.mm = mm;
        this.year = year;
        this.cvc = cvc;
    }

    public void setProxy(String host, int port, String login, String pass){
        IProxy proxy = new Proxy(host, port, login, pass);
        proxy.setStatus(ProxyStatus.VALID);
        proxies.add(proxy);
    }

    public void setProxy(IProxy proxy){
        proxy.setStatus(ProxyStatus.VALID);
        proxies.add(proxy);
    }

    public void run() {
        try {
            log.info("Start get profiles");
            List<IProfile> profiles = new LinkedList<>();
            profiles.add(Profile
                    .builder()
                    .email(getRandomElement(emails))
                    .firstname(getRandomElement(first_names))
                    .lastname(getRandomElement(last_names))
                    .city("Москва")
                    .zipcodeIndex("190000")
                    .street(getRandomElement(address))
                    .houseNumber(String.valueOf(getRandom(1, 150)))
                    .apartmentNumber(String.valueOf(getRandom(1, 2500)))
                    .phoneNumber("+7(914)" + getRandom(111, 999) + "-" + getRandom(11, 99) + "-" + getRandom(11, 99))
                    .card(card_number)
                    .mm(mm)
                    .year(year)
                    .cvc(cvc)
                    .build());
            log.info("Get proxies");
            TaskGroup taskGroup = new TaskGroup();
            taskGroup.setItemId(item_id);
            taskGroup.setProfiles(profiles);
            taskGroup.setProxies(proxies);
            taskGroup.setFilter(AvailabilityFilters.MAX_EFFECT);
            taskGroup.setTasksType(module);
            taskGroup.setAmount(1);
            log.info("run");
            List<ITask> task = taskGroup.start();
            while (!task.get(0).getFuture().isDone() && !task.get(0).getFuture().isCancelled()) {
                for (ITask itask : task) {
                    itask.update();
                    if (!itask.isChecked()) {
                        if (itask.getStatus() == TaskStatus.SUCCESS) {
                            System.out.println("SUCCESS");
                            System.out.println(itask.getMessage());
                            itask.setChecked(true);
                        } else if (itask.getStatus() == TaskStatus.ERROR) {
                            System.out.println("ERROR");
                            System.out.println(itask.getMessage());
                            itask.setChecked(true);
                        } else {
                            System.out.println(itask.getLogs());
                        }
                    }
                }
                Thread.sleep(100);
            }
            taskGroup.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
