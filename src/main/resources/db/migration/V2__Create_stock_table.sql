-- Create stock table
CREATE TABLE IF NOT EXISTS stock (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sku VARCHAR(100) NOT NULL,
    location_id UUID NOT NULL,
    qty_on_hand INTEGER NOT NULL DEFAULT 0,
    reserved_qty INTEGER NOT NULL DEFAULT 0,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_location FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE,
    CONSTRAINT uk_stock_sku_location_tenant UNIQUE (sku, location_id, tenant_id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_stock_sku_location ON stock(sku, location_id, tenant_id);
CREATE INDEX IF NOT EXISTS idx_stock_sku_tenant ON stock(sku, tenant_id);

-- Create trigger for updated_at
DROP TRIGGER IF EXISTS update_stock_updated_at ON stock;
CREATE TRIGGER update_stock_updated_at
    BEFORE UPDATE ON stock
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

