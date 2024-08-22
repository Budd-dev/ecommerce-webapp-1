import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

function CartPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const { cart: initialCart, products, orderId: initialOrderId, selectedPhoneNo, orderDate: initialOrderDate, userId } = location.state || {}; // Get userId from state
    const [cart, setCart] = useState(initialCart || {});
    const [orderId, setOrderId] = useState(initialOrderId || null);
    const [orderDate, setOrderDate] = useState(initialOrderDate || null);

    console.log("User ID:", userId);
    console.log("Phone number", selectedPhoneNo);

    useEffect(() => {
        if (!orderId) {
            createOrder();
        }
    }, []);

    const createOrder = async () => {
        try {
            const orderItems = Object.keys(cart).map(productId => ({
                productId: parseInt(productId),
                quantity: cart[productId]
            }));

            const payload = {
                userId: userId,
                orderItems: orderItems
            };
            const response = await axios.post("http://localhost:8080/api/ecommerce/orders/", payload);
            setOrderId(response.data.orderId);
            setOrderDate(response.data.date);
        } catch (error) {
            console.error("Error creating order:", error.response ? error.response.data : error.message);
        }
    };

    const getProductById = (id) => products.find(product => product.productId === id);

    const getTotalPrice = () => {
        return Object.keys(cart).reduce((total, productId) => {
            const product = getProductById(parseInt(productId));
            const quantity = cart[productId] || 0;
            return total + (product ? product.price * quantity : 0);
        }, 0);
    };

    const goToProductsPage = () => {
        navigate('/products', { state: { selectedPhoneNo, userId } });
    };

    const proceedToPayment = () => {
        navigate('/payment', { state: { selectedPhoneNo, userId , cart, orderId, orderDate } });
    };

    return (
        <div className="container mt-4">
            <h2>Cart</h2> 
            {orderId && orderDate && (
                <div className="alert alert-info">
                    <strong>Order ID:</strong> {orderId} <br />
                    <strong>Date:</strong> {orderDate}
                </div>
            )}
            <div className="row">
                {Object.keys(cart).map(productId => {
                    const product = getProductById(parseInt(productId));
                    if (!product || cart[productId] <= 0) return null;

                    return (
                        <div key={product.productId} className="col-md-4 mb-4">
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title">{product.name}</h5>
                                    <p className="card-text">{product.description}</p>
                                    <p className="card-text">Price: ₹{product.price}</p>
                                    <p className="card-text">Quantity: {cart[product.productId] || 0}</p>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
            <div className="mt-4">
                <h4>Total Price: ₹{getTotalPrice().toFixed(2)}</h4>
                <button className="btn btn-primary mr-2" onClick={goToProductsPage}>Back to Products</button>
                <button className="btn btn-success" onClick={proceedToPayment}>Proceed to Payment</button>
            </div>
        </div>
    );
}

export default CartPage;
