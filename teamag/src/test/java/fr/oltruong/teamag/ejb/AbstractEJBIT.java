package fr.oltruong.teamag.ejb;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractEJBIT {

    // ======================================
    // = Attributes =
    // ======================================
    private static EJBContainer ejbContainer;

    private static Context context;

    // ======================================
    // = Lifecycle Methods =
    // ======================================

    @BeforeClass
    public static void initContainer() throws Exception {
        // Map<String, Object> properties = Maps.newHashMap();
        // properties.put(EJBContainer.MODULES, new File("target/teamag/WEB-INF/classes"));
        ejbContainer = EJBContainer.createEJBContainer();
        context = ejbContainer.getContext();
    }

    @AfterClass
    public static void closeContainer() throws Exception {
        if (ejbContainer != null) {
            ejbContainer.close();
        }
    }

    public static Context getContext() {
        return context;
    }

}
