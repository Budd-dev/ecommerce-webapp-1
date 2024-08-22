import React from "react";

const productImages = {
    1: '/air.jpeg',
    2: '/camera.jpg',
    3: '/computer.png',
    // Add mappings for all products here
};

function ImageTest() {
    return (
        <div> <img src={`${productImages[1]}`} />
                                {/* <img src="/air.jpeg" alt="Air Test" /> */}
        </div>
    );
}

export default ImageTest;