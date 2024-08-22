import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

function PaymentPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const { selectedPhoneNo, userId , cart, orderId, orderDate } = location.state || {};
    const [paymentType, setPaymentType] = useState("");
    const [paymentResponse, setPaymentResponse] = useState(null);
    // const { userId, selectedPhoneNo } = location.state || {};

    console.log("User ID:", userId);
    console.log("phone number", selectedPhoneNo);

    const handlePaymentTypeChange = (event) => {
        setPaymentType(event.target.value);
    };

    const goToMainMenu = () => {
        navigate('/main-menu', { state: { selectedPhoneNo, userId } });  // Pass both userId and phoneNumber
    };
    

    const handlePayment = async () => {
        if (!paymentType) {
            alert("Please select a payment method.");
            return;
        }

        try {
            const response = await axios.post("http://localhost:8080/api/ecommerce/payments/", {
                type: paymentType,
                status: "Completed", // You can set this based on actual payment gateway response
                transactionId: "TRX" + Math.floor(Math.random() * 1000000), // Generate a mock transaction ID
                orderId: orderId,
            });

            setPaymentResponse(response.data);
        } catch (error) {
            console.error("Error processing payment:", error);
            alert("Payment failed. Please try again.");
        }
    };

    // const goToCartPage = () => {
    //     navigate('/cart', { state: { cart, orderId, orderDate } });
    // };

    return (
        <div className="container mt-4">
            <h2>Payment Page</h2>
            {orderId && orderDate && (
                <div className="alert alert-info">
                    <strong>Order ID:</strong> {orderId} <br />
                    <strong>Order Date:</strong> {orderDate}
                </div>
            )}
            <div className="order-list">
                <label htmlFor="paymentType">Select Payment Method:</label>
                <select
                    id="paymentType"
                    className="form-control"
                    value={paymentType}
                    onChange={handlePaymentTypeChange}
                >
                    <option value="">-- Select Payment Method --</option>
                    <option value="Credit Card">Credit Card</option>
                    <option value="PayPal">PayPal</option>
                    <option value="Bank Transfer">Bank Transfer</option>
                </select>
            </div>
            <button className="btn btn-primary mt-3" onClick={handlePayment}>Make Payment</button>

            {paymentResponse && (
                <div className="mt-4">
                    <h4>Payment Success</h4>
                    <div className="alert alert-success">
                        <strong>Payment ID:</strong> {paymentResponse.paymentId} <br />
                        <strong>Transaction ID:</strong> {paymentResponse.transactionId} <br />
                        <strong>Payment Method:</strong> {paymentResponse.type} <br />
                        <strong>Status:</strong> {paymentResponse.status} <br />
                        <strong>Order ID:</strong> {paymentResponse.orderId}
                    </div>
                </div>
            )}

            <button className="btn btn-secondary mt-3" onClick={goToMainMenu}>Back to Main Menu</button>
        </div>
    );
}

export default PaymentPage;
