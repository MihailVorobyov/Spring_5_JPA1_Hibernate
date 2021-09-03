package ru.vorobyov.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ProductDao {
	private static SessionFactory factory;
	private Session session;
	
	public ProductDao() {
		factory = new Configuration()
			.configure("hibernate.cfg.xml")
			.addAnnotatedClass(Product.class)
			.buildSessionFactory();
	}
	
	public static void main(String[] args) {
		
		ProductDao productDao = new ProductDao();
		
		// Проверка 1
		System.out.println(productDao.findById(3L));
		
		// Проверка 2
//		System.out.println(productDao.findAll());
		
		// Проверка 3
//		Product phone = new Product("Huawei SuperPhone", 79000);
//		System.out.println(productDao.saveOrUpdate(phone));
//		phone.setPrice(75000);
//		System.out.println(productDao.saveOrUpdate(phone));
		
		// Проверка 4
//		productDao.deleteById(9L);
		
	}
	
	public Product findById(Long id) {
		session = factory.getCurrentSession();
		
		session.beginTransaction();
		Product product = session.find(Product.class, id);
		
		return product;
	}
	
	public List<Product> findAll() {
		session = factory.getCurrentSession();
		
		session.beginTransaction();
		List<Product> productList = session.createQuery("from Product").getResultList();
		
		return productList;
	}
	
	public void deleteById(Long id) {
		session = factory.getCurrentSession();
		
		session.beginTransaction();
		Product productToBeDeleted = session.get(Product.class, id);
		session.delete(productToBeDeleted);
		session.getTransaction().commit();
	}
	
	public Product saveOrUpdate(Product product) {
		session = factory.getCurrentSession();
		Product productFromDB;
		
		session.beginTransaction();
		List<Product> productList = session.createQuery("from Product p where p.title = :title and p.price = :price")
			.setParameter("title", product.getTitle())
			.setParameter("price", product.getPrice())
			.getResultList();

		if (productList.isEmpty()) {
			productFromDB = product;
		} else if (productList.size() == 1) {
			productFromDB = productList.get(0);
		} else {
			productFromDB = null;
		}
		session.saveOrUpdate(product);
		session.getTransaction().commit();

		return productFromDB;
	}
}
