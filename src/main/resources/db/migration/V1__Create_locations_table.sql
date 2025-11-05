-- Create locations table
CREATE TABLE IF NOT EXISTS locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    address TEXT,
    tenant_id UUID NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_locations_name_tenant UNIQUE (name, tenant_id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_locations_tenant ON locations(tenant_id);

-- Create updated_at trigger function (if not exists)
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger for updated_at
DROP TRIGGER IF EXISTS update_locations_updated_at ON locations;
CREATE TRIGGER update_locations_updated_at
    BEFORE UPDATE ON locations
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

