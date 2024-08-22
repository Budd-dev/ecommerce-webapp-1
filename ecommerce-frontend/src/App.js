import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import UserDetailsPage from './pages/UserDetailsPage';
import MainMenuPage from './pages/MainMenuPage'; 
import ProductsPage from './pages/ProductsPage';
import OrdersPage from './pages/OrdersPage'; 
import CartPage from './pages/CartPage'; 
import PaymentPage from './pages/PaymentPage'; 
import AuthPage from './pages/AuthPage';   


import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

const Gets = (props) => {
  return (
  <div>
    <h1>Hiii</h1>
    <h3>{props.name}</h3>
    <h3>{props.age}</h3>
    </div>)
};

function Job(props) {
  return (
    <div>
      <h3>{props.salary}</h3>
      <h3>{props.position}</h3>
      <h3>{props.company}</h3>
    </div>
  )
}

function App() {
  return (
    <div className="App"> 
    {/* <Gets name = "Shama Zafar" age = {26} /> */}
    {/* <Job salary = {90000} position="SDE" company="Licious" /> */}
    <Router>
      <Routes>
        <Route path="/main-menu" element={<MainMenuPage />} /> 
        <Route path="/user-details/:phoneNo" element={<UserDetailsPage />} />
        <Route path="/products" element={<ProductsPage />} />  {/* Add ProductsPage */}
        <Route path="/orders/:userId" element={<OrdersPage />} />
        <Route path="/cart" element={<CartPage />} />  {/* Add OrdersPage */}
        <Route path="/payment" element={<PaymentPage />} />
        <Route path="/auth" element={<AuthPage />} />
      </Routes>
    </Router>
    </div>
  );
}

export default App;