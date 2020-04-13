package local.tin.tests.dao.impl;

import local.tin.tests.dao.ProductDAOConfiguration;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import local.tin.tests.model.domain.exceptions.DAOException;
import org.apache.log4j.Logger;

public class ProductDAOFactory extends AbstractDAOFactory {

    private static final Object LOCK = new Object();
    private static final Logger LOGGER = Logger.getLogger(ProductDAOFactory.class);
    private static volatile EntityManagerFactory entityManagerFactory;

    private ProductDAOFactory() {
    }

    private static class Loader {

        protected static final ProductDAOFactory INSTANCE = new ProductDAOFactory();

        private Loader() {
        }
    }

    public static ProductDAOFactory getInstance() {
        ProductDAOFactory receiptDAOFactory = Loader.INSTANCE;
        EntityManagerFactory emf = entityManagerFactory;
        if (emf == null) {
            synchronized (LOCK) {
                emf = entityManagerFactory;
                if (emf == null) {
                    try {
                        entityManagerFactory = Persistence.createEntityManagerFactory(ProductDAOConfiguration.getInstance().getPersistenceUnit());
                    } catch (DAOException ex) {
                        LOGGER.error("Unexpected DAOException creating entity manager factory: " + ex.getLocalizedMessage());
                        LOGGER.debug("Unexpected DAOException creating entity manager factory: " + ex.getLocalizedMessage(), ex);
                    }
                }
            }
        }
        return receiptDAOFactory;

    }

    @Override
    protected EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    protected String getDAOBasePackage() throws DAOException {
        return ProductDAOConfiguration.getInstance().getDAOBasePackage();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

}