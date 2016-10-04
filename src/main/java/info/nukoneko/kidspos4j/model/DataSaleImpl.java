package info.nukoneko.kidspos4j.model;

import info.nukoneko.kidspos4j.util.config.BarcodeCreator;
import rx.Observable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by atsumi on 2016/02/03.
 */
final public class DataSaleImpl extends DataBase<ModelSale> {

    @Override
    String QueryCreate() {
        return "CREATE TABLE  IF NOT EXISTS " + getTableKind().getName() +
                "(id INTEGER  PRIMARY KEY AUTOINCREMENT, " +
                "barcode TEXT UNIQUE, " +
                "created_at TEXT, " +
                "points INTEGER, " +
                "price INTEGER, " +
                "items TEXT, " +
                "store INTEGER, " +
                "staff INTEGER)";
    }

    @Override
    String QueryInsert(ModelSale item) {
        return String
                .format("INSERT INTO %s (barcode, created_at, points, price, items, store, staff) " +
                                "VALUES('%s','%s', %d, %d, '%s', %d, %d)",
                        getTableKind().getName(),
                        item.getBarcode(),
                        item.getCreatedAt(),
                        item.getPoints(),
                        item.getPrice(),
                        item.getItems(),
                        item.getStoreId(),
                        item.getStaffId());
    }

    @Override
    String QueryUpdate(ModelSale item) {
        return String
                .format("UPDATE %s " +
                                "SET barcode = '%s'," +
                        "created_at = '%s', " +
                        "points = '%s', " +
                        "price = '%s', " +
                        "items = '%s', " +
                        "store = '%s', " +
                        "staff = '%s' " +
                        "WHERE id = '%s'",
                        getTableKind().getName(),
                        item.getBarcode(),
                        item.getCreatedAt(),
                        item.getPoints(),
                        item.getPrice(),
                        item.getItems(),
                        item.getStoreId(),
                        item.getStaffId(),
                        item.getId()
                );
    }

    @Override
    public Observable<ModelSale> findAllRx() {
        ArrayList<ModelSale> list = find(ModelSale.class);
        return Observable.from(list.toArray(new ModelSale[list.size()]));
    }

    @Override
    public ArrayList<ModelSale> findAll() {
        return find(ModelSale.class);
    }

    @Override
    public ArrayList<ModelSale> find(String where) {
        return find(ModelSale.class, where);
    }

    @Override
    public ModelSale findFirst(String where) {
        ArrayList<ModelSale> arrayList =
                find(ModelSale.class, where);
        if (arrayList.size() == 0) {
            return null;
        }
        return arrayList.get(0);
    }

    @Override
    public ModelSale findFromBarcode(String barcode) {
        return findFirst(String.format("barcode = '%s'", barcode));
    }

    @Override
    void setValues(ModelSale model, ResultSet rs) throws SQLException {
        model.setId(rs.getInt("id"));
        model.setBarcode(rs.getString("barcode"));
        model.setCreatedAt(rs.getString("created_at"));
        model.setPoints(rs.getInt("points"));
        model.setPrice(rs.getInt("price"));
        model.setItems(rs.getString("items"));
        model.setStoreId(rs.getInt("store"));
        model.setStaffId(rs.getInt("staff"));
    }

    @Override
    TableKind getTableKind() {
        return TableKind.SALE;
    }

    //TODO: ModelItem[] から, points, price, items, を取得
    public ModelSale createNewSale(Integer points,
                                   Integer prices,
                                   String items,
                                   Integer storeId,
                                   Integer staffId) {
        String barcode =
                BarcodeCreator.create(
                        BarcodeCreator.BARCODE_PREFIX.SALE,
                        storeId, getNewItemId());

        System.out.println(barcode);

        ModelSale ret = new ModelSale();
        ret.setBarcode(barcode);
        ret.setPrice(prices);
        ret.setCreatedAt(getNowTime());
        ret.setStoreId(storeId);
        ret.setItems(items);
        ret.setPoints(points);
        ret.setStaffId(staffId);

        if (insert(ret)) {
            return findFromBarcode(barcode);
        }
        return null;
    }

    public int getNewItemId(){
        int last = findAll().size();
        if (last > 0) {
            String _bar = findAll().get(last - 1).getBarcode();
            if (_bar == null){
                _bar = String.valueOf(findAll().get(last - 1).getId());
            }
            _bar = _bar.substring(_bar.length() - BarcodeCreator.MAX_ITEM_LENGTH);

            System.out.println(_bar);
            if (_bar == null){
                return findAll().size();
            } else {
                try {
                    return (Integer.parseInt(_bar) + 1);
                }
                catch (Exception e){
                    return findAll().size();
                }
            }
        } else {
            return  1;
        }
    }
}
