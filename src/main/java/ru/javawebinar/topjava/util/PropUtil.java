package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.GenericDao;
import ru.javawebinar.topjava.model.Meal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public class PropUtil {
    private static final Logger log = getLogger(PropUtil.class);
    private static final Properties properties = new Properties();

    public static String getProperty(String property) throws IOException {
        InputStream input = PropUtil.class.getClassLoader().getResourceAsStream("app.properties");

        if (input == null) {
            log.debug("Sorry, unable to find app.properties");
            throw new NoSuchFileException("app.properties");
        }

        properties.load(input);

        if (properties.containsKey(property)) {
            log.debug(property + "=" + properties.getProperty(property));
            return properties.getProperty(property);
        }

        log.debug(property + " not found");
        throw new NoSuchElementException(property);
    }

    public static GenericDao<?> getDao(String T) throws ClassNotFoundException, IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (GenericDao<?>) Class.forName("ru.javawebinar.topjava.dao." + getProperty("db.driver" + T)).getConstructors()[0].newInstance();
    }

}
