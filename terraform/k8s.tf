#resource "aws_iam_group" "kops" {
#  name = "kops"
#}

resource "aws_iam_policy_attachment" "kops" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2FullAccess"
}

resource "aws_iam_policy_attachment" "kops-route53" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/AmazonRoute53FullAccess"
}

resource "aws_iam_policy_attachment" "kops-s3" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}

resource "aws_iam_policy_attachment" "kops-iam" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/IAMFullAccess"
}

resource "aws_iam_policy_attachment" "kops-vpc" {
  name       = "kops"
  groups     = ["kops"]
  policy_arn = "arn:aws:iam::aws:policy/AmazonVPCFullAccess"
}

#resource "aws_iam_user" "kops" {
#  name = "kops"
#}

resource "aws_iam_group_membership" "kops" {
  name = "kops"
  users = [
    "kops",
  ]
  group = "kops"
}

resource "aws_s3_bucket" "kops" {
  #bucket = "gdp-devops-k8s.nonprod.aws.dentsufusion.com"
  bucket = "gdp-devops-test"
  region = "ca-central-1"
}
