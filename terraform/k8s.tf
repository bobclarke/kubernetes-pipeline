resource "aws_iam_group" "kops" {
  name = "kops"
}

resource "aws_iam_policy_attachment" "kops-ec2" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2FullAccess"
  depends_on = ["aws_iam_group.kops"]
}

resource "aws_iam_policy_attachment" "kops-route53" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/AmazonRoute53FullAccess"
  depends_on = ["aws_iam_group.kops"]
}

resource "aws_iam_policy_attachment" "kops-s3" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
  depends_on = ["aws_iam_group.kops"]
}

resource "aws_iam_policy_attachment" "kops-iam" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/IAMFullAccess"
  depends_on = ["aws_iam_group.kops"]
}

resource "aws_iam_policy_attachment" "kops-vpc" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/AmazonVPCFullAccess"
  depends_on = ["aws_iam_group.kops"]
}

resource "aws_iam_group_membership" "kops" {
  name = "kops"
  users = [
    "kops",
  ]
  group = "kops"
  depends_on = ["aws_iam_group.kops"]
}

resource "aws_s3_bucket" "kops_config_bucket" {
  bucket = "gdp-devops-k8s-nonprod-aws-dentsufusion-com"
  region = "us-east-1"
}
