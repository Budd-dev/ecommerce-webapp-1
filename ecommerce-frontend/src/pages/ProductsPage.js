import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";

function ProductsPage() {
    const location = useLocation();
    const { selectedPhoneNo, userId } = location.state || {}; // Get userId from the state
    const [products, setProducts] = useState([]);
    const [cart, setCart] = useState({});
    const [orderId, setOrderId] = useState(null);
    const [orderDate, setOrderDate] = useState(null);
    const navigate = useNavigate();

    console.log("User ID:", userId);
    console.log("phone number", selectedPhoneNo);

    // Array holding the image filenames
    const productImages = [
        '/hdd2.jpeg',
        '/camera.jpeg',
        '/computer.png',
        '/console1.jpeg',
        '/headphones1.jpeg',
        '/tv.jpeg',
        '/laptop.jpeg',
        '/phone.avif',
        '/speaker.jpeg',
        '/watch.jpeg',
    ];

    const goToMainMenu = () => {
        navigate('/main-menu', { state: { selectedPhoneNo, userId } });
    };

    useEffect(() => {
        async function fetchProducts() {
            const response = await axios.get("http://localhost:8080/api/ecommerce/products/");
            setProducts(response.data);
        }
        fetchProducts();
    }, []);

    const addToCart = async (productId) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/ecommerce/products/${productId}/quantity`);
            const availableQuantity = response.data;
    
            if (availableQuantity > (cart[productId] || 0)) {
                setCart(prevCart => ({
                    ...prevCart,
                    [productId]: (prevCart[productId] || 0) + 1
                }));
            } else {
                alert("Not enough stock available");
            }
        } catch (error) {
            console.error("Error fetching product quantity:", error);
        }
    };

    const removeFromCart = (productId) => {
        setCart(prevCart => ({
            ...prevCart,
            [productId]: Math.max((prevCart[productId] || 0) - 1, 0)
        }));
    };

    const calculateTotalPrice = () => {
        const total = products.reduce((total, product, index) => {
            const quantity = cart[product.productId] || 0;
            return total + (product.price * quantity);
        }, 0);
        
        return total.toFixed(2);
    };

    const viewCart = () => {
        navigate('/cart', { state: { cart, products, orderId, selectedPhoneNo, orderDate, userId } }); // Pass userId to Cart page
    };

    return (
        <div className="container mt-4" >
            <h2>Products</h2>
            <div className="row">
                {products.map((product, index) => {
                    const isLastItem = index === products.length - 1;
                    const isOutOfStock = product.quantity === 0;

                    return (
                        <div
                            key={product.productId}
                            className={`col-md-4 mb-4 ${isLastItem ? 'offset-md-4' : ''}`}  // Center if last item
                        >
                            <div className="hey">
                                <img 
                                    src={productImages[index]} 
                                    className="card-img-top" 
                                    alt={product.name} 
                                    style={{ width: "100%", height: "280px", objectFit: "contain" }} 
                                />
                                <div className="card-body">
                                    <h5 className="card-title" style={{ fontWeight: 'bold' }}>{product.name}</h5>
                                    <p></p>
                                    <p className="card-text product-description">{product.description}</p>
                                    <p className="card-text">₹{product.price}</p>
                                    {isOutOfStock ? (
                                        <p className="text-danger">OUT OF STOCK!</p>
                                    ) : (
                                        <div className="d-flex align-items-center justify-content-center">
                                            <button 
                                                className="btn btn-primary mr-2" 
                                                onClick={() => addToCart(product.productId)}
                                            >
                                                +
                                            </button>
                                            <span className="mx-2">{cart[product.productId] || 0}</span>
                                            <button 
                                                className="btn btn-danger ml-2" 
                                                onClick={() => removeFromCart(product.productId)}
                                            >
                                                -
                                            </button>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
            <div className="mt-4">
                <h4>Total Price: ₹{calculateTotalPrice()}</h4>
            </div>
            <button className="btn btn-dark mt-4" onClick={viewCart}>Place Order  </button> <div></div> 
            <button className="btn btn-dark mt-4" onClick={goToMainMenu}>  Back</button>
        </div>
    );
}

export default ProductsPage;
