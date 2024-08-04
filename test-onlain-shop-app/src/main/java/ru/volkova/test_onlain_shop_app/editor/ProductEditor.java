package ru.volkova.test_onlain_shop_app.editor;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.volkova.test_onlain_shop_app.entity.Product;
import ru.volkova.test_onlain_shop_app.repository.ProductRepository;

@SpringComponent
@UIScope
public class ProductEditor extends VerticalLayout implements KeyNotifier {
    private final ProductRepository repository;
    private Product product;

    TextField productName = new TextField("Product name");
    TextField price = new TextField("Price");

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    Binder<Product> binder = new Binder<>(Product.class);
    private ChangeHandler changeHandler;


    @Autowired
    public ProductEditor(ProductRepository repository) {
        this.repository = repository;

        add(productName, price, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editProduct(product));
        setVisible(false);
    }

    void delete() {
        repository.delete(product);
        changeHandler.onChange();
    }

    void save() {
        repository.save(product);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editProduct(Product p) {
        if (p == null) {
            setVisible(false);
            return;
        }
        if (p.getId() != null) {
            product = repository.findById(p.getId()).orElse(p);
        }
        else {
            product = p;
        }

        binder.setBean(product);

        setVisible(true);

        productName.focus();
    }

    public void setChangeHandler(ChangeHandler h) {

        changeHandler = h;
    }
}
