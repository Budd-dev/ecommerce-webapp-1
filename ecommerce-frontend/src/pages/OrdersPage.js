import React, { useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";

function OrdersPage() {
    const { userId } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const { selectedPhoneNo } = location.state || {};
    const [orders, setOrders] = useState([]);

    console.log("User ID:", userId);
    console.log("phone number", selectedPhoneNo);

    useEffect(() => {
        // Fetch orders for the user based on userId
        fetch(`http://localhost:8080/api/ecommerce/orders/user/${userId}`)
            .then((response) => response.json())
            .then((data) => setOrders(data))
            .catch((error) => console.error("Error fetching orders:", error));
    }, [userId]);

    const handleBackToMainMenu = () => {
        navigate("/main-menu", { state: { selectedPhoneNo, userId } });
    };

    return (
        <div className="container mt-4">
            <h2 className="text-center">Your Orders</h2>
            {orders.length > 0 ? (
                <div className="order-list">
                    {orders.map((order) => (
                        <div key={order.orderId} className="card mb-3">
                            <div className="card-header bg-dark text-white">
                                <h5 className="mb-0">Order ID: {order.orderId}</h5>
                            </div>
                            <div className="card-body">
                                <p><strong>Date:</strong> {order.date}</p>
                                <p><strong>Status:</strong> {order.status}</p>
                                <p><strong>Total Price:</strong> ₹{order.totalPrice.toFixed(2)}</p>
                                <h6 className="mt-3">Items:</h6>
                                <ul className="list-group">
                                    {order.orderItems.map((item) => (
                                        <li key={item.itemId} className="list-group-item">
                                            <div className="d-flex justify-content-between">
                                                <span>Product ID: {item.productId}</span>
                                                <span>Quantity: {item.quantity}</span>
                                                <span>Total Price: ₹{item.totalPrice.toFixed(2)}</span>
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <p className="text-center">No orders found for this user.</p>
            )}
            <div className="text-center mt-4">
            <button className="btn btn-dark mt-4" onClick={handleBackToMainMenu}>Back</button>
            </div>
        </div>
    );
}

export default OrdersPage;
