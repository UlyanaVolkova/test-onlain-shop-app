package ru.volkova.test_onlain_shop_app.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.volkova.test_onlain_shop_app.entity.Product;
import ru.volkova.test_onlain_shop_app.editor.ProductEditor;
import ru.volkova.test_onlain_shop_app.repository.ProductRepository;

@Route
public class MainView extends VerticalLayout {
    private final ProductRepository productRepository;
    private ProductEditor editor;
    private final Grid<Product> grid = new Grid<>(Product.class);
    private final TextField filter = new TextField("", "Type to filter");
    private final Button addNewBtn = new Button("Add new", VaadinIcon.PLUS.create());

@Autowired
    public MainView(ProductRepository productRepository, ProductEditor productEditor) {
        this.productRepository = productRepository;
        this.editor = productEditor;
    HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
    add(actions, grid, editor);


    grid.setHeight("200px");
    grid.setColumns("id", "productName", "price");
    grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

    filter.setPlaceholder("Filter by productName");

    filter.setValueChangeMode(ValueChangeMode.EAGER);
    filter.addValueChangeListener(e -> listProducts(e.getValue()));

    grid.asSingleSelect().addValueChangeListener(e -> {
        editor.editProduct(e.getValue());
    });

    addNewBtn.addClickListener(e -> editor.editProduct(new Product()));

    editor.setChangeHandler(() -> {
        editor.setVisible(false);
        listProducts(filter.getValue());
    });
    showProduct("");
}

private void showProduct(String productName){
    if(productName.isEmpty()) {
        grid.setItems(productRepository.findAll());
    }else {
        grid.setItems(productRepository.findByProductNameStartsWithIgnoreCase(productName));
    }
}

    void listProducts (String filterText){

        if (StringUtils.hasText(filterText)) {
            grid.setItems(productRepository.findByProductNameStartsWithIgnoreCase(filterText));
        } else {
            grid.setItems(productRepository.findAll());
        }
    }
}
