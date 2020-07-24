import React from 'react';
import '../modal.css'

const modal = (props) => {
    return (
        <div>
            <div className="modal-wrapper"
                style={{
                    transform: props.show ? 'translateY(0vh)' : 'translateY(-100vh)',
                    opacity: props.show ? '5' : '0'
                }}>
                <div className="modal-header">
                    <h6>Alert</h6>
                    <span className="close-modal-btn" onClick={props.close}>×</span>
                </div>
                <div className="modal-body">
                    <p>
                        {props.children}
                    </p>
                </div>
            
            </div>
        </div>
    )
}

export default modal