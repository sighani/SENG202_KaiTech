package kaitech.io;

import kaitech.api.model.Supplier;
import kaitech.util.PhoneType;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LoadSuppliersTest {
    //testing class for loading the supplier data with supplierLoader

    //storing loaded files
    private Map<String, Supplier> suppliersLoaded;


    //cleaning before each test

    @Before
    public void testLoadFile(){
        //path to supplier test files
        String fileName = "resources/data/Suppliers.xml";
        //new supplier loader instance
        LoadData.loadSuppliers(fileName);
        suppliersLoaded = LoadData.supplierList();

        //make sure we loaded all suppliers
        assertEquals("All suppliers in the file should be added", 4, suppliersLoaded.size());
    }

    @Test
    public void testSupplierID(){
        String supID = this.suppliersLoaded.get("s1").id();
        assertEquals("Checking the first suppliers Id is correct", "s1", supID);
    }

    @Test
    public void testName(){
        String testName = this.suppliersLoaded.get("s2").name();
        assertEquals("Checking supplier 2's name", "Funky Kumquat", testName);
    }

    @Test
    public void testAddress(){
        String testAddr = this.suppliersLoaded.get("s4").address();
        assertEquals("Testing supplier 4's address is correctly parsed", "Unit 5, Bert's Business Park, Christchurch", testAddr);
    }

    @Test
    public void testPhoneNumber(){
        String ph1 = this.suppliersLoaded.get("s3").phone();
        assertEquals("Checking if s3 phone number is correctly parsed", "021345666", ph1);
    }

    @Test
    public void testPhoneType(){
        PhoneType testPhoneType = this.suppliersLoaded.get("s1").phoneType();
        assertEquals("Checking if s1 phone type is correctly parsed", PhoneType.WORK, testPhoneType);
    }

    @Test
    public void testEmail(){
        String testEmail = this.suppliersLoaded.get("s1").email();
        assertEquals("Testing eamil of supplier 1 is correctly parsed", "daveG@countup.co.nz", testEmail);
    }

    @Test
    public void testUrl(){
        String testUrlS3 = this.suppliersLoaded.get("s3").url();
        String testUrlS4 = this.suppliersLoaded.get("s4").url();
        assertEquals("Testing supplier 3 loaded email correctly", "cfoods.co.nz", testUrlS3);
        assertEquals("testing supplier 4 has unknown email as no data provided", Supplier.UNKNOWN_URL, testUrlS4);
    }
}
