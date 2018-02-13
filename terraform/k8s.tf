provider "aws" {
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
  region     = "${var.aws_region}"
}

# Create a route53 zone
resource "aws_route53_zone" "main" {
  name = "bobclarke.uk"
}

# Now add a subdomain
resource "aws_route53_zone" "dev" {
  name = "dev.bobclarke.uk"

  tags {
    Environment = "dev"
  }
}

resource "aws_route53_record" "dev-ns" {
  zone_id = "${aws_route53_zone.main.zone_id}"
  name    = "dev.bobclarke.uk"
  type    = "NS"
  ttl     = "30"

  records = [
    "${aws_route53_zone.dev.name_servers.0}",
    "${aws_route53_zone.dev.name_servers.1}",
    "${aws_route53_zone.dev.name_servers.2}",
    "${aws_route53_zone.dev.name_servers.3}",
  ]
}

resource "aws_s3_bucket" "k8s" {
  bucket = "bucket.dev.bobclarke.uk"
  acl    = "public"

  tags {
    Name        = "bucket.dev.bobclarke.uk"
    Environment = "Dev"
  }
}
