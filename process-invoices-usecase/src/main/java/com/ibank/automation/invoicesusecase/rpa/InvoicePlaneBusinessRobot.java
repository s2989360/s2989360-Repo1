package com.ibank.automation.invoicesusecase.rpa;

import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.intake.api.domain.Document;
import com.workfusion.intake.api.domain.Field;
import com.ibank.automation.system.invoiceplane.InvoicePlaneClient;
import com.ibank.automation.system.invoiceplane.page.CreateProductPage;
import com.ibank.automation.system.invoiceplane.page.LoginPage;
import com.ibank.automation.system.invoiceplane.page.MainPage;
import com.ibank.automation.system.invoiceplane.page.MenuNavigationBar;
import com.ibank.automation.system.invoiceplane.page.ProductsPage;
import com.ibank.automation.system.invoiceplane.to.ProductTO;
import com.workfusion.rpa.core.security.SecurityUtils;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InvoicePlaneBusinessRobot {

	public static final String INVOICEPLANE_CREDENTIALS_ALIAS = "invoiceplane_credentials_alias";
	
	private final SecurityUtils securityUtils;
	
	private final Logger logger;
	
	private static final int EXPECTED_PRODUCTS_COUNT = 20;
	
	private Map<String, String> params;
	
	private MenuNavigationBar menuNavigationBar;

	public InvoicePlaneBusinessRobot(final SecurityUtils securityUtils, final Logger logger, Map<String, String> params) {
		this.logger = logger;
		this.securityUtils = securityUtils;
		this.params = params;
	}

	public ProductsPage addProduct(ProductTO product) {
		initRobot();
		final CreateProductPage createProductPage = menuNavigationBar.openCreateProduct();
		ProductsPage productsPage = createProductPage.addProduct(product);
		logger.debug("Adding product: " + product.getProductName());
		finiliseRobot();
		return productsPage;
	}

	public List<Document> parseProductsToDocuments() {
		initRobot();
		final List<ProductTO> products = parseProducts();
		logger.debug("Extracted products count: " + products.size());
		finiliseRobot();
		return products.stream().map(this::mapProductToDocument).limit(EXPECTED_PRODUCTS_COUNT)
				.collect(Collectors.toList());
	}

	private MainPage initRobot() {
		final InvoicePlaneClient client = new InvoicePlaneClient(logger, params);
		final LoginPage loginPage = client.getLoginPage();

		final SecureEntryDTO loginCreds = getLoginCreds();
		final MainPage mainPage = loginPage.login(loginCreds);

		this.menuNavigationBar = new MenuNavigationBar(logger);

		return mainPage;
	}

	private SecureEntryDTO getLoginCreds() {
		final SecureEntryDTO secureEntry = securityUtils.getSecureEntry(INVOICEPLANE_CREDENTIALS_ALIAS);

		if (secureEntry == null) {
			throw new IllegalStateException(
					"Could not get credentials from Secret Vault. Please set 'admin@workfusion.com' as key, 'o66Lc1Jn6Z' as value for '"
							+ INVOICEPLANE_CREDENTIALS_ALIAS + "' alias in your Secret Vault");
		}
		return secureEntry;
	}

	private List<ProductTO> parseProducts() {
		final ProductsPage productsPage = menuNavigationBar.openProducts();
		final List<ProductTO> products = new ArrayList<ProductTO>();

		while (needMoreProductsAndHasSmthToParse(productsPage, products)) {
			products.addAll(productsPage.getProducts().stream()
					.filter(distinctByKey(p -> p.getProductName().toLowerCase())).collect(Collectors.toList()));
		}

		return products;
	}

	private String uuid() {
		return UUID.randomUUID().toString();
	}

	private void finiliseRobot() {
		if (menuNavigationBar != null) {
			menuNavigationBar.logout();
			menuNavigationBar = null;
		}
	}

	private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	private boolean needMoreProductsAndHasSmthToParse(ProductsPage productsPage, List<ProductTO> products) {
		return !(products.size() > EXPECTED_PRODUCTS_COUNT || !productsPage.nextPage());
	}

	private Document mapProductToDocument(ProductTO productTO) {
		Document document = new Document();
		document.setId(uuid());
		document.setName(productTO.getProductName());
		document.getExtractedFields().put("family", Field.of(productTO.getFamily()));
		document.getExtractedFields().put("price", Field.of(productTO.getPrice()));
		document.getExtractedFields().put("description", Field.of(productTO.getProductDescription()));
		document.getExtractedFields().put("product_name", Field.of(productTO.getProductName()));
		document.getExtractedFields().put("sku", Field.of(productTO.getSku()));
		document.getExtractedFields().put("tax_rate", Field.of(productTO.getTaxRate()));
		document.getExtractedFields().put("index", Field.of(Long.toString(productTO.getIndex())));
		return document;
	}

}