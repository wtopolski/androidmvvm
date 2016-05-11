package pogadanka.androidmvvm.viewmodel;

import pogadanka.androidmvvm.model.SimpleForm;

/**
 * Created by wojciechtopolski on 05/05/16.
 */
public class SimpleFormViewModel {
    public ButtonViewModel myButton;

    public SimpleFormViewModel() {
        myButton = new ButtonViewModel();
    }

    public void setSimpleForm(SimpleForm form) {
        myButton.setText(form.getButtonName());
    }

    public void enableInterface(boolean enabled) {
        myButton.setVisibility(enabled);
    }
}
