import static org.junit.Assert.*;

import org.junit.Test;

import com.jfinal.ext2.kit.SqlKit;

public class SqlKitTest {

	@Test
	public void test() {
		SqlKit kit = new SqlKit();
		kit.select("*").from("user").where("user=1").and("name='2'").ascOrderBy("ii").limit("1,12");
		System.out.println(kit.sql());
	}

}
