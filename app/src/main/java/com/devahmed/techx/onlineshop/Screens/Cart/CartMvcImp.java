package com.devahmed.techx.onlineshop.Screens.Cart;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devahmed.techx.onlineshop.Common.MVC.BaseObservableMvcView;
import com.devahmed.techx.onlineshop.Models.DeliverCost;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Screens.Cart.CartItem.CartListAdapter;
import java.util.ArrayList;
import java.util.List;

public class CartMvcImp extends BaseObservableMvcView<CartMvc.Listener> implements CartMvc, CartListAdapter.Listener {

    private RecyclerView cartRecyclerView;
    private Button checkoutBtn , goToShoppingBtn;
    private TextView totalPriceText , emptyCartText , deliveryCostText , totalPriceTextView;
    private List<Product> cartProductList;
    private List<String> cartProductsCountList;
    private CartListAdapter adapter;
    private ProgressBar progressBar;
    private ImageView emptyCartPlaceHolder;
    private double totalPrice = 0;
    private double deliveryCost = 0;
    private List<DeliverCost> deliverCostList;

    public CartMvcImp(LayoutInflater inflater , ViewGroup group) {
        setRootView(inflater.inflate(R.layout.cart_fragment , group , false));

        initViews();

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalEarnedPoints = 0;
                for(Product product : cartProductList){
                    int indexOfProduct = cartProductList.indexOf(product);
                    int count = Integer.parseInt(cartProductsCountList.get(indexOfProduct));
                    totalEarnedPoints += product.getPoints() * count;
                }
                for(Listener listener : getmListeners()){
                    listener.onSubmitBtnClicked(totalEarnedPoints);
                }

            }
        });
        goToShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onGoToShoppingBtnClicked();
                }
            }
        });

        totalPriceTextView.setText(getContext().getResources().getString(R.string.total));
        emptyCartText.setText(getContext().getResources().getString(R.string.empty_cart));
        checkoutBtn.setText(getContext().getResources().getString(R.string.checkout));
    }

    private void initViews() {
        cartRecyclerView = findViewById(R.id.cartRecycler);
        totalPriceTextView = findViewById(R.id.totalPeiceTextView);
        checkoutBtn = findViewById(R.id.cartCheckoutBtn);
        totalPriceText = findViewById(R.id.cartTotalPriceText);
        progressBar = findViewById(R.id.progressBar);
        deliveryCostText = findViewById(R.id.deliveryCostText);
        emptyCartText = findViewById(R.id.emptyCartText);
        emptyCartPlaceHolder = findViewById(R.id.emptycartplaceholder);
        goToShoppingBtn = findViewById(R.id.cartGoToShoppingBtn);
        cartProductList = new ArrayList<>();
        adapter = new CartListAdapter(cartProductList , this , getContext());
        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(adapter);
        this.deliverCostList = new ArrayList<>();
    }

    @Override
    public void bindDeliveryCost(List<DeliverCost> deliverCosts) {
        this.deliverCostList = deliverCosts;
        updateDeliveryCostRange(deliverCosts);
    }

    public void updateDeliveryCostRange(List<DeliverCost> deliverCosts){
        int newDeliverCost = 0;
        for (int i = 0; i <deliverCosts.size() ; i++) {
            //get the range that this order is in
            if(totalPrice >= deliverCosts.get(i).getFrom() && totalPrice <= deliverCosts.get(i).getTo()){
                newDeliverCost = deliverCosts.get(i).getPrice();
                if(this.deliveryCost != newDeliverCost){
                    //remove old delivery cost
                    this.totalPrice -= this.deliveryCost;
                    //add the new delivery cost to the total price
                    this.totalPrice += newDeliverCost;
                    //update our delivery cost now
                    this.deliveryCost = newDeliverCost;
                }

                break;
            }
        }
        //update the text on screen
        this.deliveryCostText.setText(getContext().getResources().getString(R.string.delivery_cost) + " : " + newDeliverCost + "-EG");
    }

    @Override
    public void bindCartData(List<Product> productList ,  List<String> productsCount) {
        this.cartProductList = productList;
        this.cartProductsCountList = productsCount;
        adapter.setList(productList);
        for(int i = 0; i < productList.size(); i++){
            totalPrice += productList.get(i).getPrice() * Integer.parseInt(productsCount.get(i));
        }
        totalPriceText.setText("" + totalPrice + "-EGP");
        if(productList.isEmpty()){
            activateEmptyCartState();
        }
        onIncreaseBtnClicked(productList.get(0));
        onDecreaseBtnCLicked(productList.get(0));
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void activateEmptyCartState() {
        emptyCartPlaceHolder.setVisibility(View.VISIBLE);
        goToShoppingBtn.setVisibility(View.VISIBLE);
        emptyCartText.setVisibility(View.VISIBLE);
        deliveryCostText.setVisibility(View.GONE);
        checkoutBtn.setEnabled(false);
        totalPriceText.setText("0-EGP");
    }

    @Override
    public void onIncreaseBtnClicked(Product product) {
        this.totalPrice += product.getPrice();
        updateDeliveryCostRange(this.deliverCostList);
        totalPriceText.setText("" + totalPrice + "-EGP");
        int index = cartProductList.indexOf(product);
        int newValue = Integer.parseInt(cartProductsCountList.get(index)) + 1;
        cartProductsCountList.set(index , String.valueOf(newValue));
    }

    @Override
    public void onDecreaseBtnCLicked(Product product) {
        this.totalPrice -= product.getPrice();
        updateDeliveryCostRange(this.deliverCostList);
        totalPriceText.setText("" + totalPrice + "-EGP");
        int index = cartProductList.indexOf(product);
        int newValue = Integer.parseInt(cartProductsCountList.get(index)) - 1;
        cartProductsCountList.set(index , String.valueOf(newValue));
        if(totalPrice == deliveryCost){
            activateEmptyCartState();
        }
    }

    @Override
    public void onProductImageClicked(Product product) {
        for(Listener listener : getmListeners()){
            listener.onCartItemImageClicked(product);
        }
    }

    @Override
    public double getTotalPrice() {
        return this.totalPrice;
    }

    @Override
    public double getDeliveryCost() {
        return this.deliveryCost;
    }

    public ArrayList<String> getProductsIds(){
        ArrayList<String> productsIds = new ArrayList<String>();
        for(Product product : cartProductList ){
            productsIds.add(product.getId());
        }
        return productsIds;
    }

    public ArrayList<Integer> getProductsCounts(){
        ArrayList<Integer> productsCountList = new ArrayList<>();
        for(String countAsString : cartProductsCountList){
            productsCountList.add(Integer.parseInt(countAsString));
        }
        return productsCountList;
    }

    @Override
    public boolean isCartEmpty() {
        if(totalPrice == deliveryCost){
            return true;
        }
        return false;
    }
}