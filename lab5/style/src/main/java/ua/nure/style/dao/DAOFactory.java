package ua.nure.style.dao;

public class DAOFactory {

    private final ConnectionFactory connectionFactory;

    public DAOFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private static IMyDao dao;

    public IMyDao getDao(DAOType type) {
        if (dao == null) {
            switch (type) {
                case MYSQL:
                    dao = new MysqlDAO();
                    return dao;
                case MONGO:
                    dao = new MongoDBDAO();
                    return dao;
            }
        }
        return dao;
    }
}
