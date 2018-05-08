kubernetes-pipeline
===================

Experimenting with building an Ephemeral CI/CD pipleine in a box

Work in progress

### To spin up a project
```
clone this repo 
cd helm-charts 
helm upgrade --install <project name> stack1-ci --namespace <project name> --set platform=<minikube|aws> --set repo=<repo url>

exmaple:
helm upgrade --install test-project stack1-ci --namespace test-project --set platform=minikube --set repo=https://github.com/bobclarke/test-project.git

To delete the project:
helm delete --purge test-project
```


### Todo
IAM Stuff to be automated
```
aws iam create-group --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonEC2FullAccess --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonRoute53FullAccess --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/IAMFullAccess --group-name kops
aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonVPCFullAccess --group-name kops

aws iam create-user --user-name kops
aws iam add-user-to-group --user-name kops --group-name kops
aws iam create-access-key --user-name kops

```
### Terraform
```
For all files which match terraform.tfvars or *.auto.tfvars present in the current directory, Terraform automatically loads them to populate variables. If you populate this with you access key and secret key you'll obviously want to put an entry in your .gitignore so you don't push them to scm. Something like this will do:

**/terraform.tfvars
**/*.auto.tfvars

```

### Kops config 

```
apiVersion: kops/v1alpha2
kind: Cluster
metadata:
  creationTimestamp: 2018-03-24T21:25:17Z
  name: cluster1.bobclarke.info
spec:
  additionalPolicies:
    master: |
      [
        {
            "Effect": "Allow",
            "Action": [
                "route53:ChangeResourceRecordSets"
            ],
            "Resource": [
                "arn:aws:route53:::hostedzone/*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "route53:ListHostedZones",
                "route53:ListResourceRecordSets"
            ],
            "Resource": [
                "*"
            ]
        }
      ]
          node: |
      [
        {
            "Effect": "Allow",
            "Action": [
                "route53:ChangeResourceRecordSets"
            ],
            "Resource": [
                "arn:aws:route53:::hostedzone/*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "route53:ListHostedZones",
                "route53:ListResourceRecordSets"
            ],
            "Resource": [
                "*"
            ]
        }
      ]
  api:
    dns: {}
  authorization:
    alwaysAllow: {}
  channel: stable
  cloudProvider: aws
  configBase: s3://k8s-bobclarke-info-state-store/cluster1.bobclarke.info
  etcdClusters:
  - etcdMembers:
    - instanceGroup: master-us-east-1a
      name: a
    name: main
  - etcdMembers:
    - instanceGroup: master-us-east-1a
      name: a
    name: events
  iam:
    allowContainerRegistry: true
    legacy: false
      kubernetesApiAccess:
  - 0.0.0.0/0
  kubernetesVersion: 1.8.7
  masterInternalName: api.internal.cluster1.bobclarke.info
  masterPublicName: api.cluster1.bobclarke.info
  networkCIDR: 172.20.0.0/16
  networking:
    kubenet: {}
  nonMasqueradeCIDR: 100.64.0.0/10
  sshAccess:
  - 0.0.0.0/0
  subnets:
  - cidr: 172.20.32.0/19
    name: us-east-1a
    type: Public
    zone: us-east-1a
  topology:
    dns:
      type: Public
    masters: public
    nodes: public
```
