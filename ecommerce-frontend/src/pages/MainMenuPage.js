import React from "react";
import { useNavigate, useLocation } from "react-router-dom";

function MainMenuPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const { selectedPhoneNo, userId } = location.state || {};

    console.log("User ID:", userId);
    console.log("phone number", selectedPhoneNo);


    const handleLogout = () => {
        navigate('/auth'); // Redirect to the Login page
    };

    return (
        <div className="container mt-4">
            <div className="form-box">
                <h2>Main Menu</h2>
                <div className="menu-btn" role="group">
                    <button
                        className="list-group-item list-group-item-action"
                        onClick={() => navigate(`/user-details/${selectedPhoneNo}`, { state: {userId }})}
                    >
                        View Profile
                    </button>
                </div>
                <div className="menu-btn" role="group">
                    <button
                        className="list-group-item list-group-item-action"
                        onClick={() => navigate("/products", { state: { selectedPhoneNo, userId } })}
                    >
                        Products
                    </button>
                </div>
                <div className="menu-btn" role="group">
                    <button
                        className="list-group-item list-group-item-action"
                        onClick={() => navigate(`/orders/${userId}`, { state: { selectedPhoneNo } })}
                    >
                        Orders
                    </button>
                </div>
                    
                <button
                    className="btn btn-dark mt-4"
                    onClick={handleLogout}
                >
                    Logout
                </button>
            </div>
        </div>
    );
}

export default MainMenuPage;
