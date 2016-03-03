import info.nukoneko.kidspos4j.exception.CannotCreateItemException;
import info.nukoneko.kidspos4j.model.DataBase;
import info.nukoneko.kidspos4j.model.DataItemImpl;
import info.nukoneko.kidspos4j.model.ItemFactory;
import info.nukoneko.kidspos4j.model.ModelItem;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by atsumi on 2016/02/03.
 */
public class ItemFactoryTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ItemFactory.getInstance().truncate();
    }

    private ModelItem getModel(String barcode){
        ModelItem item = new ModelItem();
        item.setName("Yamada");
        item.setBarcode(barcode);
        item.setPrice(500);
        item.setStoreId(10);
        item.setGenreId(1);
        return item;
    }

    public void testInsert(){
        DataBase<ModelItem> model = ItemFactory.getInstance();
        assertNotNull(model);
        assertTrue(model.findAll().isEmpty());
        assertEquals(model.findAll().size(), 0);
        assertEquals((Integer) model.findAll().size(),model.findAllRx().count().toBlocking().single());

        ModelItem item = getModel("12345");
        assertNull(item.getId());
        assertTrue(model.insert(item));

        assertEquals(model.findAll().size(), 1);
        assertEquals((Integer) model.findAll().size(),model.findAllRx().count().toBlocking().single());

        ModelItem modelItem = model.findAll().get(0);
        assertNotNull(modelItem);
        assertNotNull(modelItem.getId());
        assertEquals(modelItem.getId(), (Integer)1);
        assertEquals(modelItem.getBarcode(), "12345");
        assertFalse(model.insert(item));

        ModelItem item2 = getModel("67891");
        assertNull(item2.getId());
        assertTrue(model.insert(item2));

        assertEquals(model.findAll().size(), 2);
        assertEquals((Integer) model.findAll().size(),model.findAllRx().count().toBlocking().single());

        ModelItem modelItem2 = model.findAll().get(1);
        assertNotNull(modelItem2);
        assertNotNull(modelItem2.getId());
        assertEquals(modelItem2.getId(), (Integer)2);
        assertEquals(modelItem2.getBarcode(), "67891");
        assertFalse(model.insert(item2));
    }

    public void testSearch(){
        DataBase<ModelItem> model = ItemFactory.getInstance();
        ModelItem item = getModel("12345");
        model.insert(item);
        ModelItem item2 = getModel("67891");
        model.insert(item2);

        assertEquals(model.findAll().size(), 2);
        ArrayList<ModelItem> list = model.find("barcode = '12345'");
        assertEquals(list.size(), 1);
        assertEquals(model.find("barcode = '67891'").size(), 1);

        ModelItem item3 = list.get(0);
        assertNotSame(item.getId(), item3.getId());
        assertEquals(item.getName(), item3.getName());
        assertEquals(item.getBarcode(), item3.getBarcode());
        assertEquals(item.getGenreId(), item3.getGenreId());
        assertEquals(item.getPrice(), item3.getPrice());
    }

    public void testCreate() throws CannotCreateItemException {
        DataItemImpl model = ItemFactory.getInstance();
        ModelItem item = getModel("12345");
        ModelItem item1 = model.createNewItem(item.getName(), item.getStoreId(), item.getGenreId(), item.getPrice());
        assertNotNull(item1);

        assertNotSame(item.getId(), item1.getId());
        assertEquals(item.getName(), item1.getName());
        assertNotSame(item.getBarcode(), item1.getBarcode());
        assertEquals(item.getGenreId(), item1.getGenreId());
        assertEquals(item.getPrice(), item1.getPrice());

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ItemFactory.getInstance().truncate();
    }
}
