-- Create reservations table
CREATE TABLE IF NOT EXISTS reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    sku VARCHAR(100) NOT NULL,
    location_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reservation_location FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_reservations_order ON reservations(order_id, tenant_id);
CREATE INDEX IF NOT EXISTS idx_reservations_status_expires ON reservations(status, expires_at);
CREATE INDEX IF NOT EXISTS idx_reservations_sku_location ON reservations(sku, location_id);

-- Create trigger for updated_at
DROP TRIGGER IF EXISTS update_reservations_updated_at ON reservations;
CREATE TRIGGER update_reservations_updated_at
    BEFORE UPDATE ON reservations
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

