-- Create stock_adjustments table
CREATE TABLE IF NOT EXISTS stock_adjustments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    stock_id UUID NOT NULL,
    delta INTEGER NOT NULL,
    reason VARCHAR(50) NOT NULL,
    order_id UUID,
    user_id UUID NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_adjustment_stock FOREIGN KEY (stock_id) REFERENCES stock(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_adjustments_stock ON stock_adjustments(stock_id);
CREATE INDEX IF NOT EXISTS idx_adjustments_order ON stock_adjustments(order_id);

