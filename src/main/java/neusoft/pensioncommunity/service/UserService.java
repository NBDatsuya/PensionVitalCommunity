package neusoft.pensioncommunity.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.dao.UserDao;
import neusoft.pensioncommunity.model.User;


public class UserService implements Service<User>{
    @Getter
    private static final UserService instance = new UserService();

    private UserService(){}
    private static final UserDao dao = UserDao.getInstance();

    public User verifyUser(String name, String pwd){
        for(User item : dao.getAll())
            if (item.getName().equals(name) && item.getPassword().equals(pwd))
                return item;

        return null;
    }

    public ObservableList<User> searchByRole(int role, boolean bool){
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll())
            if((item.getRole()==role)^(!bool))
                list.add(item);

        return list;
    }
    public ObservableList<User> searchByName(String name){
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll())
            if((item.getName().equals(name)))
                list.add(item);

        return list;
    }
    public ObservableList<User> searchByRealName(String realName){
        return null;
    }
    public ObservableList<User> searchByTel(String tel){
        return null;
    }
    public ObservableList<User> searchByGender(int gender) {
        return null;
    }
    //SearchByDate?
    @Override
    public void save() {
        dao.save();
    }

    @Override
    public int size() {
        return dao.size();
    }

    @Override
    public int getNewId() {
        return 0;
    }

    public User searchById(int id) {
        return dao.search(id);
    }

    public void remove(int id){
        dao.remove(id);
    }

    @Override
    public User search(int id) {
        return null;
    }

    @Override
    public void modify(int id, User item) {
        dao.modify(id,item);
    }

    @Override
    public ObservableList<User> getAll() {
        return dao.getAll();
    }

    @Override
    public void load() {
        dao.load();
    }

    public void add(User item){
        dao.add(item);
    }

}
