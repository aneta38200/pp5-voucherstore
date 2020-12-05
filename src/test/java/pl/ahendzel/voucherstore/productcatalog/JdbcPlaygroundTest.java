package pl.ahendzel.voucherstore.productcatalog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JdbcPlaygroundTest {

    public static final String PRODUCT_ID = "fe902a9c-b713-4007-a54d-e0cb89284bb7";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate.execute("DROP TABLE products_catalog__products IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE `products_catalog__products` (" +
                "`id` varchar(100) NOT NULL," +
                "`description` varchar(255)," +
                "`picture` varchar(150)," +
                "`price` DECIMAL(12,2)," +
                "PRIMARY KEY (id)" +
                ");");
    }

    @Test
    public void itCountProducts() {
        int result = jdbcTemplate.queryForObject("Select count(*) from `products_catalog__products`",Integer.class);

        assertThat(result).isEqualTo(0);

    }

    @Test
    public void itAddProduct() {
        var query = "INSERT INTO `products_catalog__products` (`id`, `description`, `picture`, `price`) values " +
                "('p1', 'p1 description', 'p1 picture', 20.20)," +
                "('p2', 'p2 description', 'p2 picture', 20.40)" +
                ";";

        jdbcTemplate.execute(query);

        int result = jdbcTemplate.queryForObject("Select count(*) from `products_catalog__products`",Integer.class);
        assertThat(result).isEqualTo(2);

    }

    @Test
    public void itLoadProduct() {
        var query = "INSERT INTO `products_catalog__products` (`id`, `description`, `picture`, `price`) values " +
                "('fe902a9c-b713-4007-a54d-e0cb89284bb7', 'p1 description', 'p1 picture', 20.20)," +
                "('fe902a9c-b713-4007-a54d-e0cb89284bb8', 'p2 description', 'p2 picture', 20.40)" +
                ";";

        jdbcTemplate.execute(query);
        var selectQuery = "Select * from `products_catalog__products` where id = ?";
        Product product = jdbcTemplate.queryForObject(selectQuery, new Object[] {PRODUCT_ID}, new ProductRowMapper());

        assertThat(product.getId()).isEqualTo(PRODUCT_ID);
    }

    @Test
    public void itLoadProductMapViaLambda() {
        var query = "INSERT INTO `products_catalog__products` (`id`, `description`, `picture`, `price`) values " +
                "('fe902a9c-b713-4007-a54d-e0cb89284bb7', 'p1 description', 'p1 picture', 20.20)," +
                "('fe902a9c-b713-4007-a54d-e0cb89284bb8', 'p2 description', 'p2 picture', 20.40)" +
                ";";

        jdbcTemplate.execute(query);
        var selectQuery = "Select * from `products_catalog__products` where id = ?";
        Product product = jdbcTemplate.queryForObject(selectQuery, new Object[] {PRODUCT_ID}, (rs, i) -> {
            Product p = new Product(UUID.fromString(rs.getString("id")));
            p.setDescription(rs.getString("description"));

            return p;
        });

        assertThat(product.getId()).isEqualTo(PRODUCT_ID);
    }

    @Test
    public void itAllowToAddProduct() {
        Product newProduct = new Product(UUID.randomUUID());
        newProduct.setDescription("abc");

        jdbcTemplate.update(
                "INSERT INTO `products_catalog__products` (`id`, `description`, `picture`, `price`) values " +
                        "(?,?,?,?)",
                    newProduct.getId(), newProduct.getDescription(), newProduct.getPicture(), newProduct.getPrice());

        List<Product> products = jdbcTemplate.query("Select * from `products_catalog__products`", new ProductRowMapper());

        assertThat(products)
                .hasSize(1)
                .extracting(Product::getId)
                .contains(newProduct.getId());



    }


    class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet resultSet, int i) throws SQLException {
            Product product = new Product(UUID.fromString(resultSet.getString("id")));
            product.setDescription(resultSet.getString("description"));

            return product;
        }
    }

}
